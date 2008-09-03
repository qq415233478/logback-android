package ch.qos.logback.classic.pattern;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.qos.logback.classic.pattern.lru.Event;
import ch.qos.logback.classic.pattern.lru.T_LRUCache;

public class Simulator {

  
  Random random;
  
  int worldSize;
  int get2PutRatio;
  
  public Simulator(int worldSize, int get2PutRatio) {
    this.worldSize = worldSize;
    this.get2PutRatio = get2PutRatio;
    long seed = System.nanoTime();
    System.out.println("seed is "+seed);
    random = new Random(seed);
  }
  
  public List<Event> generateScenario(int len) {
    List<Event> scenario = new ArrayList<Event>();
    
    for(int i = 0; i < len; i++) {
      
      int r = random.nextInt(get2PutRatio);
      boolean put = false;
      if(r == 0) {
        put = true;
      }
      r = random.nextInt(worldSize);
      Event<String> e = new Event<String>(put, String.valueOf(r));
      scenario.add(e);
    }
    return scenario;
  }
  
  public void simulate(List<Event> scenario, LRUCache<String, String> lruCache, T_LRUCache<String> tlruCache) {
    for(Event<String> e: scenario) {
      if(e.put) {
        lruCache.put(e.k, e.k);
        tlruCache.put(e.k);
      } else {
        String r0 = lruCache.get(e.k);
        String r1 = tlruCache.get(e.k);
        if(r0 != null) {
          assertEquals(r0, e.k);
        }
        assertEquals(r0, r1);
      }
    }
  }
  
//  void compareAndDumpIfDifferent(LRUCache<String, String> lruCache, T_LRUCache<String> tlruCache) {
//    lruCache.dump();
//    tlruCache.dump();
//    if(!lruCache.keyList().equals(tlruCache.ketList())) {
//      lruCache.dump();
//      tlruCache.dump();
//      throw new AssertionFailedError("s");
//    }
//  }
}