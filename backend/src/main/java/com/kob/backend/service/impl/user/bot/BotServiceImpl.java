package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.bot.BotService;
import com.kob.backend.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BotServiceImpl implements BotService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> addBot(Map<String, String> data) {
        User user = LoginUserUtil.getUser();
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

        if (title == null || title.length() == 0) {
            map.put("error_message", "title can't be null");
            return map;
        }

        if (title.length() > 100) {
            map.put("error_message", "title is too long");
            return map;
        }

        if (description == null && description.length() > 300) {
            map.put("error_message", "description is too long");
            return map;
        }

        if (content == null || content.length() == 0) {
            map.put("error_message", "Code can't be null");
            return map;
        }

        if (content.length() > 10000) {
            map.put("error_message", "code is too long");
            return map;
        }

        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, 1500, now, now);

        botMapper.insert(bot);
        map.put("error_message", "success");

        return map;
    }

    @Override
    public Map<String, String> removeBot(Map<String, String> data) {
        User user = LoginUserUtil.getUser();
        int bot_id = Integer.parseInt(data.get("bot_id"));

        Bot bot = botMapper.selectById(bot_id);
        Map<String, String> map = new HashMap<>();

        if (bot == null) {
            map.put("error_message", "Bot is not existed");
            return map;
        }

        if (!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "This is not your bot");
            return map;
        }

        botMapper.deleteById(bot_id);
        map.put("error_message", "success");

        return map;
    }

    @Override
    public Map<String, String> updateBot(Map<String, String> data) {
        User user = LoginUserUtil.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

        if (title == null || title.length() == 0) {
            map.put("error_message", "title can't be null");
            return map;
        }

        if (title.length() > 100) {
            map.put("error_message", "title is too long");
            return map;
        }

        if (description == null && description.length() > 300) {
            map.put("error_message", "description is too long");
            return map;
        }

        if (content == null || content.length() == 0) {
            map.put("error_message", "Code can't be null");
            return map;
        }

        if (content.length() > 10000) {
            map.put("error_message", "code is too long");
            return map;
        }

        Bot bot = botMapper.selectById(bot_id);
        if (bot == null) {
            map.put("error_message", "Bot is not existed");
            return map;
        }

        if (!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "This is not your bot");
            return map;
        }

        Bot new_bot = new Bot(bot.getId(), user.getId(), title, description, content, bot.getRating(), bot.getCreatetime(), new Date());
        botMapper.updateById(new_bot);
        map.put("error_message", "success");
        return map;
    }

    @Override
    public List<Bot> getBotList() {
        User user = LoginUserUtil.getUser();

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());

        return botMapper.selectList(queryWrapper);
    }
}
