local cjson = require("cjson.safe")
cjson.encode_empty_table_as_object(false)
local jwtSecretKey = "YourVeryveryJWTSecret"
local internalSecretKey="YourveryveryverySecretInternalKey"
local jwt = require "resty.jwt"

local resp = require("resp")
local requests = require("requests")

local M = {}
local _conf = nil

local function jwtSign(playloadbody)
  return jwt:sign(
    jwtSecretKey,
    {
      header = {typ = "JWT", alg = "HS256"},
      payload = playloadbody
    }
  )
end

local function get_token(code, state)
  local payload = {
    client_id = _conf.client_id,
    client_secret = _conf.client_secret,
    code = code,
    state = state
  }
  local status, body, err = requests.jpost(_conf.token_endpoint, cjson.encode(payload))
  if not status then
    resp.fail(err, _conf.token_endpoint, ngx.HTTP_SERVICE_UNAVAILABLE)
  else
    if status ~= 200 then
      resp.fail(err, cjson.decode(body), status)
    else
      return ngx.decode_args(body).access_token
    end
  end
end

local function get_profile(token)
  local status, body, err = requests.jget(_conf.profile_endpoint, "", {Authorization = "token " .. token})
  if not status then
    resp.fail(err, _conf.profile_endpoint, ngx.HTTP_SERVICE_UNAVAILABLE)
  else
    if status ~= 200 then
      resp.fail(err, cjson.decode(body), status)
    else
      return cjson.decode(body)
    end
  end
end

local function code_url(state)
  local params =
    ngx.encode_args(
    {
      client_id = _conf.client_id,
      redirect_uri = _conf.redirect_uri,
      scope = _conf.scope,
      state = state
    }
  )
  return _conf.code_endpoint .. "?" .. params
end

function M.get_code(state)
  return ngx.redirect(code_url(state))
end

function M.authorization(code, state)
  local token = get_token(code, state)
  if token ~= nil then
    local profile = get_profile(token)
    if not profile or profile == nil then
      ngx.say('{"errors": [{"status": 401,"detail": "Unable find user profile"}]}')
      return
    else
      local expreTime=ngx.now() + 86400
      local tokenPayload = {
        iss = "MicroServices",
        sub = profile["email"],
        name = profile["name"],
        exp = expreTime
      }
      local jwt_token = jwtSign(tokenPayload)
      ngx.header["Set-Cookie"] =
        "access-token=" .. jwt_token .. "; Path=/; Expires=" .. ngx.cookie_time(expreTime)
      state = ngx.unescape_uri(state)
      state = ngx.unescape_uri(state)
      return ngx.redirect(state)
    end
  else
    resp.fail(err, "Not able to get access_token", 401)
  end
end

function M.init(conf)
  _conf = conf
end

return M
