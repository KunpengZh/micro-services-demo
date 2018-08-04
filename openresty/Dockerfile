FROM openresty/openresty:1.13.6.1-alpine
RUN apk add --no-cache curl perl
RUN opm get bungle/lua-resty-session && opm get pintsized/lua-resty-http
RUN mkdir /usr/local/openresty/nginx/conf/confs

