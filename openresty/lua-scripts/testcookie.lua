local ck = require "resty.resty-cookie"
local cookie, err = ck:new()
if not cookie then
    ngx.log(ngx.ERR, err)
    ngx.say(err)
    return
end



-- get all cookies
local fields, err = cookie:get_all()
if not fields then
    ngx.log(ngx.ERR, err)
    ngx.say(err)
    return
end

for k, v in pairs(fields) do
    ngx.say(k, " => ", v)
end

-- get single cookie
local field, err = cookie:get("access-token")
if not field then
    ngx.log(ngx.ERR, err)
    ngx.say(err)
    return
end

