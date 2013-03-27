package org.nuunframework.nuun.jeromq.topology;


public class JeroMqClientTopology extends AbstractJeroMqTopology
{

    public String name()
    {
        return "Default Cqrs Object Graph";
    }

    @Override
    protected void describe()
    {
//        // Gateway
//        newInstance(Gateway, DefaultCommandGateway.class);
//        // Command Bus
//        newInstance(CommandBus, SimpleCommandBus.class);
//        // gateway to bus
//        newReference().from(Gateway).to(CommandBus);
//        // =====================================================
//        // Aggregate Type
//        newInstance(Aggregate_Roots, AbstractAnnotatedAggregateRoot.class); // Descendent of 
//        // Event bus
//        newInstance(EventBus, SimpleEventBus.class);
//        // Event Store
//        newInstance(EventStore , FileSystemEventStore.class);
//        // Repository
//        newInstance(Repository, EventSourcingRepository.class);
//
//        // link repo to event bus and event store 
//        newReference("subscribe_repo").from(Repository).to(Aggregate_Roots);
//        newReference("subscribe_repo").from(Repository).to(EventStore);
//        newReference("subscribe_repo").from(Repository).to(EventBus);
//        
//        // Command Handling
//        newInstance(CommandHandler, AggregateAnnotationCommandHandler.class);
//        newReference("subscribe").from(CommandHandler).to(Aggregate_Roots);
//        newReference("subscribe").from(CommandHandler).to(Repository);
//        newReference("subscribe").from(CommandHandler).to(CommandBus);
//        
//        // =====================================================
//        
//        newInstance("atLeastAMethodAnnotatedWith_@EventHandler", EventHandler.class); // at a method annotated with
//        newReference().from(EventBus).to("atLeastAMethodAnnotatedWith_@EventHandler");
        
        
    }

}
