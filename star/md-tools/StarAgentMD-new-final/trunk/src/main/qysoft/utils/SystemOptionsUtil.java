package main.qysoft.utils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import main.qysoft.md.dao.UserOptionsRepository;
import main.qysoft.md.entity.IsTransDay;
import main.qysoft.md.entity.UserOptions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;

/**
 * 系统参数配置管理类
 * Created by kevin on 2017/10/25.
 */
@Component("systemOptions")
public class SystemOptionsUtil {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String GROUP_NAME = "md_group";

    public static final String OPTION_NAME = "md_config";

    /*public static final String FEE_OF_BUY = "buy_procedure_money";

    public static final String FEE_OF_SELL = "sell_procedure_money";*/

    public static final String AMPLITUDE_UP_FIRST_DAY = "first_md_up_float";

    public static final String AMPLITUDE_DOWN_FIRST_DAY = "first_md_down_float";

    public static final String AMPLITUDE_UP = "open_md_up_float";

    public static final String AMPLITUDE_DOWN = "open_md_down_float";

    public static final String FIRST_TRANS_BEGIN_TIME = "md_time_begin";

    public static final String FIRST_TRANS_END_TIME = "md_time_end";

    public static final String SECOND_TRANS_BEGIN_TIME = "md_time_begin2";

    public static final String SECOND_TRANS_END_TIME = "md_time_end2";

    public static final String THIRD_TRANS_BEGIN_TIME = "md_time_begin3";

    public static final String THIRD_TRANS_END_TIME = "md_time_end3";

    @Autowired
    UserOptionsRepository userOptionsRepository;

    private Map<String, String> optionsMap = Maps.newConcurrentMap();

    @PostConstruct
    public void init() {
        if (userOptionsRepository == null) {
            logger.error("获取系统参数Repository失败");
            return;
        }

        UserOptions mdUserOptions = userOptionsRepository.findByGroupNameAndOptionName(GROUP_NAME, OPTION_NAME);
        if (null == mdUserOptions) {
            logger.error("获取撮合交易系统参数失败");
            return;
        }

        String optionValue = mdUserOptions.getOptionValue();
        if (StringUtils.isBlank(optionValue)) {
            logger.error("撮合交易参数设置错误");
            return;
        }

        Gson gson = new Gson();
        Map options = gson.fromJson(optionValue, Map.class);
        options.forEach((key, value) -> {
            if (key instanceof String && value instanceof String) {
                optionsMap.put((String) key, (String) value);
            }
        });
    }

    /**
     * 根据参数名获取参数值
     * @param optionName
     * @return
     */
    public Optional<String> getOptionValue(String optionName) {
        if (StringUtils.isEmpty(optionName)) {
            return Optional.empty();
        }
        return Optional.of(optionsMap.get(optionName));
    }

    /**
     * 获取发布买的手续费
     * @return
     */
    /*public Optional<BigDecimal> getFeeOfBuy() {
        if (optionsMap.containsKey(FEE_OF_BUY)) {
            try {
                return Optional.of(new BigDecimal(optionsMap.get(FEE_OF_BUY)));
            } catch (NumberFormatException e) {
                logger.error("获取发布买手续费失败");
            }
        }
        return Optional.empty();
    }*/

    /**
     * 获取发布卖的手续费
     * @return
     */
    /*public Optional<BigDecimal> getFeeOfSell() {
        if (optionsMap.containsKey(FEE_OF_SELL)) {
            try {
                return Optional.of(new BigDecimal(optionsMap.get(FEE_OF_SELL)));
            } catch (NumberFormatException e) {
                logger.error("获取发布卖手续费失败");
            }
        }
        return Optional.empty();
    }*/

    /**
     * 获取首日振幅上限
     * @return
     */
    public Optional<BigDecimal> getFirstDayAmplitudeUp() {
        if (optionsMap.containsKey(AMPLITUDE_UP_FIRST_DAY)) {
            try {
                return Optional.of(new BigDecimal(optionsMap.get(AMPLITUDE_UP_FIRST_DAY)));
            } catch (NumberFormatException e) {
                logger.error("获取首日振幅上限失败");
            }
        }
        return Optional.empty();
    }

    /**
     * 获取首日振幅下限
     * @return
     */
    public Optional<BigDecimal> getFirstDayAmplitudeDown() {
        if (optionsMap.containsKey(AMPLITUDE_DOWN_FIRST_DAY)) {
            try {
                return Optional.of(new BigDecimal(optionsMap.get(AMPLITUDE_DOWN_FIRST_DAY)));
            } catch (NumberFormatException e) {
                logger.error("获取首日振幅下限失败");
            }
        }
        return Optional.empty();
    }

    /**
     * 获取振幅上限
     * @return
     */
    public Optional<BigDecimal> getAmplitudeUp() {
        if (optionsMap.containsKey(AMPLITUDE_UP)) {
            try {
                return Optional.of(new BigDecimal(optionsMap.get(AMPLITUDE_UP)));
            } catch (NumberFormatException e) {
                logger.error("获取振幅上限失败");
            }
        }
        return Optional.empty();
    }

    /**
     * 获取振幅下限
     * @return
     */
    public Optional<BigDecimal> getAmplitudeDown() {
        if (optionsMap.containsKey(AMPLITUDE_DOWN)) {
            try {
                return Optional.of(new BigDecimal(optionsMap.get(AMPLITUDE_DOWN)));
            } catch (NumberFormatException e) {
                logger.error("获取振幅下限失败");
            }
        }
        return Optional.empty();
    }

    /**
     * 获取第一段交易开始时间
     * @return
     */
    public LocalTime getFirstTransBeginTime() {
        return getConfiguredTransTime(FIRST_TRANS_BEGIN_TIME);
    }

    /**
     * 获取第一段交易结束时间
     * @return
     */
    public LocalTime getFirstTransEndTime() {
        return getConfiguredTransTime(FIRST_TRANS_END_TIME);
    }

    /**
     * 获取第二段交易开始时间
     * @return
     */
    public LocalTime getSecondTransBeginTime() {
        return getConfiguredTransTime(SECOND_TRANS_BEGIN_TIME);
    }

    /**
     * 获取第二段交易结束时间
     * @return
     */
    public LocalTime getSecondTransEndTime() {
        return getConfiguredTransTime(SECOND_TRANS_END_TIME);
    }

    /**
     * 获取第三段交易开始时间
     * @return
     */
    public LocalTime getThirdTransBeginTime() {
        return getConfiguredTransTime(THIRD_TRANS_BEGIN_TIME);
    }

    /**
     * 获取第三段交易结束时间
     * @return
     */
    public LocalTime getThirdTransEndTime() {
        return getConfiguredTransTime(THIRD_TRANS_END_TIME);
    }

    private LocalTime getConfiguredTransTime(String timePropName) {
        String endTime = optionsMap.get(timePropName);
        if (StringUtils.isBlank(endTime)) {
            return LocalTime.now();
        }

        try {
            return LocalTime.parse(endTime.trim());
        } catch (DateTimeParseException e) {
            logger.error(e.getMessage());
        }
        return LocalTime.now();
    }

    /**
     * 是否是交易日
     * @return
     */
    public String isTransDay() {
        LocalTime firstTransBeginTime = getFirstTransBeginTime();
        LocalTime firstTransEndTime = getFirstTransEndTime();
        LocalTime secondTransBeginTime = getSecondTransBeginTime();
        LocalTime secondTransEndTime = getSecondTransEndTime();
        LocalTime thirdTransBeginTime = getThirdTransBeginTime();
        LocalTime thirdTransEndTime = getThirdTransEndTime();

        LocalTime now = LocalTime.now();
        if ((now.isAfter(firstTransBeginTime) && now.isBefore(firstTransEndTime))
                || (now.isAfter(secondTransBeginTime) && now.isBefore(secondTransEndTime))
                || (now.isAfter(thirdTransBeginTime) && now.isBefore(thirdTransEndTime))) {
            return IsTransDay.YES.toString();
        }

        return IsTransDay.NO.toString();
    }
}
