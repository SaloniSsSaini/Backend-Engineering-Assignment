local current = redis.call("INCR", KEYS[1])
if tonumber(current) > tonumber(ARGV[1]) then
    return -1
end
return current