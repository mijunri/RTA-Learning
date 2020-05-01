package lstar;

import rta.Location;
import rta.RTA;
import rta.RTABuilder;
import rta.Transition;
import words.TimeWord;
import words.TimeWords;
import words.TimeWordsUtil;

import java.util.*;

public class RTAEquivalenceQuery implements EquivalenceQuery{
    private RTA rta;
    private int count;

    public RTAEquivalenceQuery(RTA rta){
        this.rta = rta;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public TimeWords findCounterExample(RTA hypotheses){
        RTA negRTA = null;
        RTA negHypothesis = null;

        try {
            negRTA = RTABuilder.getNegtiveRTA(rta);
            negHypothesis = RTABuilder.getNegtiveRTA(hypotheses);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        RTA r1 = RTABuilder.getCartesian(negRTA,hypotheses);
        TimeWords w1 = counterExample(r1);
        if(w1!=null){
            return w1;
        }

        RTA r2 = RTABuilder.getCartesian(rta,negHypothesis);
        TimeWords w2 = counterExample(r2);
        if(w2!=null){
            return w2;
        }

        return null;
    }


    private TimeWords counterExample(RTA rta){
        Set<Location> visited = new HashSet<>();
        Map<Location,TimeWords> map = new HashMap<>();
        Deque<Location> stack = new LinkedList<>();

        Location initLocation = rta.getInitLocation();
        visited.add(initLocation);
        map.put(initLocation,TimeWords.EMPTY_WORDS);
        stack.push(initLocation);
        while(!stack.isEmpty()){
            Location current = stack.pop();
            List<Transition> transitions = rta.getTransitions(current,null,null);
            for(Transition t:transitions){
                Location source = t.getSourceLocation();
                Location target = t.getTargetLocation();
                TimeWords locationWords = map.get(source);
                if(!visited.contains(target)){
                    visited.add(target);
                    TimeWord word = t.toWord();
                    TimeWords words = TimeWordsUtil.concat(locationWords,word);
                    map.put(target,words);
                    stack.push(target);
                }
            }
        }

        List<Location> acceptedLocations = rta.getAcceptedLocations();
        for(Location l:acceptedLocations){
            if(map.containsKey(l)){
                return map.get(l);
            }
        }
        return null;
    }
}