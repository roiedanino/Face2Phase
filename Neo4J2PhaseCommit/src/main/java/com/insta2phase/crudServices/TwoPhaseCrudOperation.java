package com.insta2phase.crudServices;


import com.insta2phase.crudQueries.Query;

public interface TwoPhaseCrudOperation<R extends Query>{
    boolean canExecute(R request);

    Object execute();
}
