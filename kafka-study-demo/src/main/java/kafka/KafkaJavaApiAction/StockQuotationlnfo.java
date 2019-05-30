package kafka.KafkaJavaApiAction;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * @Description 股票行情信息封装类
 * @Date 2019/5/30 0030 下午 3:42
 * @Created by Pengrenjun
 */
public class StockQuotationlnfo implements Serializable {

    //股票代码
    private String stockCode ;
    //股票名称
    private String stockName ;
    //交易时间
    private long tradeTime;
    //昨日收盘价
    private float preClosePrice;
    //开盘价
    private float openPrice ;
    //当前价，收盘时即为当日收盘价
    private float currentPrice ;
    //今日最高价
    private float ghPrice;
    //今日最低价
    private float lowPrice;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(long tradeTime) {
        this.tradeTime = tradeTime;
    }

    public float getPreClosePrice() {
        return preClosePrice;
    }

    public void setPreClosePrice(float preClosePrice) {
        this.preClosePrice = preClosePrice;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public float getGhPrice() {
        return ghPrice;
    }

    public void setGhPrice(float ghPrice) {
        this.ghPrice = ghPrice;
    }

    public float getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(float lowPrice) {
        this.lowPrice = lowPrice;
    }

    public static StockQuotationlnfo imitateCreateQuotationinfo(){
        StockQuotationlnfo quotationinfo =new StockQuotationlnfo();
        //随机产生 10 之间的整数，然后与 600100 相加组成股票代码
        Random r =new Random();
        Integer stockCode = 600100 + r.nextInt(10);
        //随机产生一个 0-1之间的浮点数
        float random= (float) Math.random();
        //设置涨跌
        if (random / 2 < 0.5) {
            random = -random;}

            DecimalFormat decimalFormat =new DecimalFormat(".00") ;//设置保存两位有效数字
            quotationinfo.setCurrentPrice(Float.valueOf(decimalFormat.format(11 +
                    random) ));// 设置最新价在 11元浮动

            quotationinfo.setPreClosePrice(11.80f);// 设置昨日收盘价为固定值
            quotationinfo.setOpenPrice(11.5f);// 设置开盘价
            quotationinfo.setLowPrice(10.5f);// 设置最低价，并不考虑 10 限制，以及当前价是否已是最低价

            quotationinfo.setGhPrice(12.5f); //设置最高价 并不考虑 10 %限制 以及当前价是否已是最高价
            quotationinfo.setStockCode(stockCode.toString());
            quotationinfo.setTradeTime(System.currentTimeMillis()) ;
            quotationinfo.setStockName("股票－ "+ stockCode);
            return quotationinfo;
    }

    @Override
    public String toString() {
        return "StockQuotationlnfo{" +
                "stockCode='" + stockCode + '\'' +
                ", stockName='" + stockName + '\'' +
                ", tradeTime=" + tradeTime +
                ", preClosePrice=" + preClosePrice +
                ", openPrice=" + openPrice +
                ", currentPrice=" + currentPrice +
                ", ghPrice=" + ghPrice +
                ", lowPrice=" + lowPrice +
                '}';
    }
}
