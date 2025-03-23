package cn.toesbieya.jxc.schedule;

import cn.toesbieya.jxc.model.entity.StatFinishOrder;
import cn.toesbieya.jxc.model.entity.StatProfitGoods;
import cn.toesbieya.jxc.model.entity.StatProfitTotal;
import cn.toesbieya.jxc.mapper.StatisticMapper;
import cn.toesbieya.jxc.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StatisticTask {
    @Resource
    private StatisticMapper mapper;

    @PostConstruct
    public void run() {
        autoStat();
    }

    //每天零点统计信息
    @Async("scheduledExecutor")
    @Scheduled(cron = "1 0 0 */1 * ?")
    public void autoStat() {
        log.info("开始统计信息...");

        long lastDay = DateUtil.getTimestampBeforeNow(1);
        long today = DateUtil.getTimestampNow();

        //检查是否已统计过昨日已完成的订单数
        if (!mapper.checkDailyFinishOrderExist(lastDay)) {
            // 采购功能已移除，使用0替代
            Integer lastDayPurchaseOrderFinish = 0;
            Integer lastDaySellOrderFinish = mapper.getSellOrderLastDayFinishOrderNum();

            StatFinishOrder param = new StatFinishOrder();
            param.setPurchase(lastDayPurchaseOrderFinish);
            param.setSell(lastDaySellOrderFinish);
            param.setTime(lastDay);

            mapper.insertFinishOrder(param);
        }

        //检查是否已统计过昨日的采购额、销售额、毛利
        if (!mapper.checkDailyProfitExist(lastDay)) {
            //昨日各个商品的采购额、销售额
            // 采购功能已移除，使用空列表替代
            List<StatProfitGoods> purchaseProfitGoods = new ArrayList<>();
            List<StatProfitGoods> sellProfitGoods = mapper.getSellOrderDailyProfitGoods(lastDay, today);

            //昨日的采购总额、销售总额
            BigDecimal totalPurchase = new BigDecimal(0);
            BigDecimal totalSell = new BigDecimal(0);

            //处理销售数据
            for (StatProfitGoods s : sellProfitGoods) {
                s.setTime(lastDay);
                s.setPurchase(new BigDecimal(0));
                BigDecimal sell = s.getSell();
                s.setProfit(sell);
                totalSell = totalSell.add(sell);
                purchaseProfitGoods.add(s);
            }

            BigDecimal totalProfit = totalSell;

            StatProfitTotal statProfitTotal = new StatProfitTotal();
            statProfitTotal.setPurchase(totalPurchase);
            statProfitTotal.setSell(totalSell);
            statProfitTotal.setProfit(totalProfit);
            statProfitTotal.setTime(lastDay);

            mapper.insertProfitTotal(statProfitTotal);

            if (!purchaseProfitGoods.isEmpty()) {
                mapper.insertProfitGoodsBatch(purchaseProfitGoods);
            }
        }

        log.info("结束统计信息...");
    }
}
