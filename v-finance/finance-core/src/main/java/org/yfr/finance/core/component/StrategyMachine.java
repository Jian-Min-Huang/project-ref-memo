package org.yfr.finance.core.component;

import org.yfr.finance.core.pojo.dto.Output;
import org.yfr.finance.core.pojo.enu.Event;
import org.yfr.finance.core.pojo.vo.Summation;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

public abstract class StrategyMachine {

    protected Integer status = 0;

    protected Deque<Output> outputs = new LinkedList<>();

    // change status
    // add output to outputs
    public abstract Optional<Output> input(Event event);

    public abstract Summation summation();

}
