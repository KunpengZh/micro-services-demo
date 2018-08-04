local cjson = require "cjson"
local jwt = require "resty.jwt"
local ck = require "resty.resty-cookie"
local resp = require("resp")
local luJson = require("resty.luaJson")

local jwtSecretKey = "YourVeryveryJWTSecret"
local internalSecretKey="YourveryveryverySecretInternalKey"
local internalSharedSecret = os.getenv("INTERNAL_SECRET")
local externalSharedSecret = os.getenv("EXTERNAL_SECRET")
local loginURL = "/auth/github/login"

assert(jwtSecretKey ~= nil, "Environment variable JWT_SECRET not set")
assert(internalSharedSecret ~= nil, "Environment variable INTERNAL_SECRET not set")
assert(externalSharedSecret ~= nil, "Environment variable EXTERNAL_SECRET not set")

local M = {}

local function getAccessToken()
    local cookie, err = ck:new()
    if not cookie then
        resp.fail("Error", "cookie library error", ngx.HTTP_SERVICE_UNAVAILABLE)
    end
    local token, err = cookie:get("access-token")
    if not token or token == nil then
        -- ngx.header["Set-Cookie"] =
        --     "access-token=yourcookievalue; Path=/; Expires=" .. ngx.cookie_time(ngx.time() + 86400)
        -- ngx.redirect("/testcookie")
        -- return
        -- return ngx.exit(ngx.HTTP_OK)
        ngx.log(ngx.NOTICE, "Unauthorized")
        return ngx.redirect(loginURL .. "?state=" .. ngx.escape_uri(ngx.var.request_uri))
    end
    return token
end

function M.auth()
    local errorJSON = '{"errors": [{"status": 401,"detail": "Unauthorized"}]}'
    local token = getAccessToken()

    -- -- ngx.say(token)
    local jwtObj = jwt:verify(jwtSecretKey, token, 0)
    -- ngx.say(luJson.table2json(jwtObj))
    if jwtObj.verified == false then
        -- ngx.status = 401
        -- ngx.say(errorJSON)
        -- return ngx.exit(ngx.HTTP_OK)
        return ngx.redirect(loginURL .. "?state=" .. ngx.escape_uri(ngx.var.request_uri))
    end
    ngx.req.set_header("app-shared-secret", internalSecretKey)
end

return M
