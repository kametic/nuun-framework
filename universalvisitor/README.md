# Overview
UniversalVisitor is a tiny but powerful library allowing to easily navigate an object graph and extracting useful information with a simple Map/Reduce API.

# Usage

You can use this library via maven using this coordinate

    <dependency>
        <groupId>org.nuunframework</groupId>
        <artifactId>universalvisitor</artifactId>
        <version>0.0.1</version>
    </dependency>

Note : for the future version we may change the groupId, stay tuned.

# API

UniversalVisitor's API is simple :
   - UniversalVisitor : the entrypoint. Will navigate through the object and create linkedlist of Node.
   - Predicate : In case of a navigation inside an object graph the number of object can be huge, so the Predicate will help choose which Field are candidate for the navigation
   - Node : a linked list of Node that will serve as input to the MapReduce. It contains the current AnnotatedElement (Field,Method, Constructor), plus some metadata,
     - Level in the tree
     -  kind Array/Set/Hash
     - then the data associated with (index or key) 
   - Mapper<T> : the mapper have to choose which kind of Node it can map (its input) , then map it and outputs the results.
   - Reducer<T,R> : the reducer take as inputs the many results of the Mapper, then reduce it to provide a usefull information.
   - MapReduce<T> : merely a composition of one Mapper and 0..n Reducer.

# Examples
## A predicate

This predicate will the UniversalVisitor only to navigate fields, which type is annotated by Alphabet.

    public class MyPredicate2 implements Predicate {
    
    	@Override
    	public boolean apply(Field input) {
    		
    		return input.getType().getAnnotation(Alphabet.class) != null;
    	}
    	
    }

## A Mapper returning Integer

This Mapper will return Integer from its mapping. Although the AnnotatedElement on wich he can work does not have to be typed Integer. A Mapper on a Field of type UUID can return a String or an Integer, this is not correlated. That is why the mapper have two methods.

    public class MyMapper implements Mapper<Integer> {
    
    	@Override
    	public Integer map(Node node)  {
    		Field f = (Field) node.accessibleObject();
    		
    		Integer value = null;
    		try {
    			value = (Integer) f.get(node.instance());
    		} catch (IllegalArgumentException e) {
    			e.printStackTrace();
    		} catch (IllegalAccessException e) {
    			e.printStackTrace();
    		}
    		
    		return value;
    	}
    
    	@Override
    	public boolean handle(AccessibleObject object) {
    		return object instanceof Field && ((Field) object).getType().equals(Integer.class);
    	}
    }

Please also note that there is no contraindication a mapper can modify the value of a Field. As multiple MapReductions can occur in one visit, it is possible that the different Mappers does not read the same value for the same field of one instance. Therefore, the order of your MapReduce job is important. 

## A Reducer

Multiple reducers can be given inside one MapReduce job. But they have to share the same input type. In the following SumReducer and MeanReducer share Integer as input type. Note that the output type can be different, even if in the example they are the same for both reducers.

    static class SumReducer implements Reducer<Integer, Integer> {
    	int counter = 0;
    	@Override
    	public void collect(Integer input) {
    		counter = counter + input;
    	}
    	@Override
    	public Integer reduce() {
    		return counter;
    	}
    }
    
    static class MeanReducer implements Reducer<Integer, Integer> {
    	int counter = 0;
    	int sum =0;
    	@Override
    	public void collect(Integer input) {
    		counter++;
    		sum +=  input;
    	}
    	@Override
    	public Integer reduce() {
    		return sum / counter;
    	}
    }

At the end of the visit, you'll have to call the reduce() method on  all your reducers. This is something, we can improve in the future.

## All Together

THe end 2 end scenario

    MyPredicate2 predicate = new MyPredicate2(); // the navigation predicate
    MyMapper2 mapper = new MyMapper2(); // outputs Integer
    SumReducer sumReducer = new SumReducer(); // inputs Integer and outputs Integer
    MeanReducer meanReducer = new MeanReducer(); // inputs Integer and outputs Integer

    // we launch the MapReduce Job
    visitor = new UniversalVisitor();

    // we create a new MapReduce job
    MapReduce<Integer> mapReduce = new MapReduce <Integer>(mapper ,sumReducer, meanReducer);
    
    // we launch the visit and the associated job
	visitor.visit(d, predicate ,  mapReduce);
    
    assertThat(sumReducer.reduce()).isEqualTo(111110);
    assertThat(meanReducer.reduce()).isEqualTo(22222);

Please also note that, the visitor can handle multiple MapReduce job of different types.

# TODO

   - MapReduce object may offer an aggregate method for Aggregation over the result of all its reducers. Ending with the addition of an Aggregation api element for that purpose.
   - UniversalVisitor will also offer the visit of Class via reflexion.
   - We also considering offering navigation of source code.
