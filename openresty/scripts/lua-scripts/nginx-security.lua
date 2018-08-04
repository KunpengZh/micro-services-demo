local cjson = require "cjson"
local jwt = require "resty.jwt"

local jwtSecret = os.getenv("JWT_SECRET")
local internalSharedSecret = os.getenv("INTERNAL_SECRET")
local externalSharedSecret = os.getenv("EXTERNAL_SECRET")

assert(jwtSecret ~= nil, "Environment variable JWT_SECRET not set")
assert(internalSharedSecret ~= nil, "Environment variable INTERNAL_SECRET not set")
assert(externalSharedSecret ~= nil, "Environment variable EXTERNAL_SECRET not set")

local M = {}

function M.auth()
    -- get the request headers
    local headers = ngx.req.get_headers()
    local sharedSecretHeaderVal = headers["app-shared-secret"]
    local apiKey = headers["api-key"]
    local errorJSON = '{"errors": [{"status": 401,"detail": "Unauthorized"}]}'
    local token = ngx.var.cookie_access_token

    if token == nil and sharedSecretHeaderVal == nil and apiKey == nil then
        ngx.status = 401
        ngx.say(errorJSON)
        return ngx.exit(ngx.HTTP_OK)
    end

    if token ~= nil then
        local jwtObj = jwt:verify(jwtSecret, token, 0)
        if jwtObj.verified == false then
            ngx.status = 401
            ngx.say(errorJSON)
            return ngx.exit(ngx.HTTP_OK)
        end
    elseif sharedSecretHeaderVal ~= nil then
        if sharedSecretHeaderVal ~= externalSharedSecret and sharedSecretHeaderVal ~= internalSharedSecret then
            ngx.status = 401
            ngx.say(errorJSON)
            return ngx.exit(ngx.HTTP_OK)
        end
    else
        local apikeys = cjson.decode(os.getenv("API_KEYS"))
        if apikeys[apiKey] == nil then
            ngx.status = 401
            ngx.say(errorJSON)
            return ngx.exit(ngx.HTTP_OK)
        else
            ngx.log(ngx.NOTICE, "clientId: " .. apikeys[apiKey])
        end
    end

    -- write the app-shared-secret header
    ngx.req.set_header('app-shared-secret', internalSharedSecret)
end

return M
