-- rateLimit 方法传入的 key 为限流接口的 ID，max 为令牌桶的最大大小，rate 为每秒钟恢复的令牌数量

local ratelimit_info = redis.pcall('HMGET', KEYS[1], 'last_time', 'current_token')

-- 上次时间、当前剩余token数量
local last_time = ratelimit_info[1]
local current_token = tonumber(ratelimit_info[2])

-- 入参
-- 令牌桶的最大容量
local max_token = tonumber(ARGV[1])

-- 每秒钟恢复的令牌数量
local token_rate = tonumber(ARGV[2])

-- 当前时间
local current_time = tonumber(ARGV[3])

-- 每生产一个token需要耗费的时间
local reverse_time = 1000 / token_rate

if current_token == nil then
    current_token = max_token
    last_time = current_time
else
    -- 当前时间和上次的时间差
    local past_time = current_time - last_time

    -- 时间差内生产的token数量，向下取整，只有到达规定时间区间才能生产token，
    -- 例如：每秒生产5个，即每200毫秒生产一个，未达到200毫秒的时间范围内都无法生产token
    local reverse_token = math.floor(past_time / reverse_time)

    -- 当前token数量，剩余token数量 + 已生产token数量
    current_token = current_token + reverse_token

    -- 这里应该是和当前时间相等
    last_time = reverse_time * reverse_token + last_time
    if current_token > max_token then
        current_token = max_token
    end
end
local result = 0

-- 当前token数量减一
if (current_token > 0) then
    result = 1
    current_token = current_token - 1
end
redis.call('HMSET', KEYS[1], 'last_time', last_time, 'current_token', current_token)
redis.call('pexpire', KEYS[1], math.ceil(reverse_time * (max_token - current_token) + (current_time - last_time)))
return result
