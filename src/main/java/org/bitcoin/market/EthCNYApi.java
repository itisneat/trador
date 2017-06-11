package org.bitcoin.market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bitcoin.common.HttpUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itisneat.collector.model.TSDBDatapoint;
import com.itisneat.collector.model.Trade;

/**
 * Created by leo on 2017/6/3.
 */
public class EthCNYApi {
    private static final String YUNBI_URL = "https://yunbi.com";
    
    private static final String CurrentKey = ".trade.current";
    private static final String[] markets = { "btccny", "ethcny", "btscny", "etccny" };
//    private static final String[] markets = { "ethcny" };
    
    public static void main(String[] args)
    {
    	final Map<String, Integer> recorders = new HashMap<String, Integer>();
    	recorders.put("btccny", 30244765);
    	recorders.put("ethcny", 30245461);
    	recorders.put("btscny", 30244767);
    	recorders.put("etccny", 30245463);
    	
    	try {
			for (int i = 0; i < args.length; i++) {
				String[] ms = args[i].split(":");
				if (ms.length == 2) {
					recorders.put(ms[0], Integer.valueOf(ms[1]));
				} else {
					System.out.println("Illegal parameter [" + args[i] + "], should like [ethcny:123456]");
					System.exit(1);
				}

			} 
		} catch (Exception e) {
			System.out.println("Illegal parameter [" + e.getMessage()+ "], should like [ethcny:123456]");
			System.exit(1);
		}
    	
    	
		List<Thread> workers = new ArrayList<Thread>();
    	for (String m : markets) {
    		final String market = m;
    		Runnable task = new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					EthCNYApi.HandleContext hc = EthCNYApi.HandleContext.getHandleContext(recorders.get(market));
					
					while(true) {
						try {
							handleCurrentTrades(market, hc);
							if (hc.count == 0) {
								Thread.sleep(5000);
							} else {
								Thread.sleep(500);
							}
						} catch (Exception e) {
							System.out.println("handleCurrentTrades failed, Error [" + e.getMessage() + "]");;
						}
					}
					
				}
			};
			Thread worker = new Thread(task);
		    worker.start();
		    workers.add(worker);
    	}
    	
    	for (Thread t : workers) {
    		try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	

    }
    
    public static List<Trade> getTrades(String market, String paramString){
    	List<Trade> trades = new LinkedList<Trade>();
    	//String trades_url = ETH_URL + "/api/v2/trades.json?market=ethcny" + paramString + "&order_by=asc";
    	String trades_url = YUNBI_URL + "/api/v2/trades.json?market=" + market + paramString + "&order_by=asc&limit=100";
    	
    	JSONArray jsonArray;
		try {
			String text = HttpUtils.getMethod(trades_url, 30000);
			jsonArray = JSONArray.parseArray(text);
		} catch (Exception e) {
			System.out.println("Get trades failed. MSG: [" + e.getMessage() + "]");
			return trades;
		}
        long time = 0l;
        int ms = 0;
                
        for(Object obj : jsonArray) {       	
        	JSONObject jObj = (JSONObject) obj;
        	Trade trade = new Trade(jObj);
        	
        	if (time == trade.getAt()) {
    			ms += 10;
    			trade.setAt(time * 1000 + ms);
    		} else {
    			ms = 0;
    		}
        	time = trade.getAt();
        	trades.add(trade);
        }
    	return trades;
    }
    
    public static void handleCurrentTrades(String market, HandleContext hc) {
    	
    	List<TSDBDatapoint> dps = new LinkedList<TSDBDatapoint>();
    	String paramterString = "";
    	if (hc.latest > 0) {
    		paramterString = "&from=" + hc.latest;
    	} 
    	List<Trade> trades = getTrades(market, paramterString);
    	
    	for(Trade trade : trades) {
    		dps.addAll(trade.toTSDPs());
    		if (hc.start == 0) {
    			//only one time
    			hc.start = trade.getId();
    		}
    	}
    	
    	if (dps.size() > 0) {
    		String openTSdbMsg = JSON.toJSONString(dps.toArray());
        	HttpUtils.appadd(openTSdbMsg);
        	Trade lastTrade = ((LinkedList<Trade>) trades).getLast();
        	hc.latest = lastTrade.getId();
        	hc.lastTradeTime = lastTrade.getCreateAt();
        	Map<String, String> map = new HashMap<String, String>();
        	map.put(String.valueOf(hc.start), String.valueOf(hc.latest));
//        	hc.jedis.hmset(lastTrade.getMarketPrefix() + CurrentKey, map);
    	}
    	
    	System.out.println("For [" + market + "], handled [" + trades.size() + "] trades, latest trade time: [" + hc.lastTradeTime + "] latest: [" + hc.latest + "], start: [" + hc.start + "]");
    	hc.count = trades.size();
    }
    
    
    static class HandleContext
    {
      int count;
      int start;
      int latest;
      String lastTradeTime;
//      Jedis jedis = new Jedis("120.55.171.131", 63791);
      
      private static ThreadLocal<HandleContext> contextHolder = new ThreadLocal<HandleContext>(){
        protected EthCNYApi.HandleContext initialValue() {
          EthCNYApi.HandleContext hc = new EthCNYApi.HandleContext();
          return hc;
        }
      };

      public static HandleContext getHandleContext(int record){
    	HandleContext hc = ((HandleContext)contextHolder.get());
    	hc.latest = record;
        return hc; 
      }

      public static void setHandleContext(HandleContext conn) {
        contextHolder.set(conn);
      }
    }

}
