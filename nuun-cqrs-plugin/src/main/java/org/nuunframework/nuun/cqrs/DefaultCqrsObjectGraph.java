package org.nuunframework.nuun.cqrs;

import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventstore.fs.FileSystemEventStore;
import org.nuunframework.kernel.api.topology.Topology;

public class DefaultCqrsObjectGraph extends Topology
{

    private static final String CommandHandler = "commandhandler";
    private static final String EventStore = "eventstore";
    private static final String EventBus = "eventbus";
    private static final String Aggregate_Roots = "aggregateRoots";
    private static final String CommandBus = "commandbus";
    private static final String Gateway = "gateway";
    private static final String Repository = "repository";

    public String name()
    {
        return "Default Cqrs Object Graph";
    }

    @Override
    protected void describe()
    {
        // Gateway
        newInstance(Gateway, DefaultCommandGateway.class);
        // Command Bus
        newInstance(CommandBus, SimpleCommandBus.class);
        // gateway to bus
        newReference().from(Gateway).to(CommandBus);
        // =====================================================
        // Aggregate Type
        newInstance(Aggregate_Roots, AbstractAnnotatedAggregateRoot.class); // Descendent of 
        // Event bus
        newInstance(EventBus, SimpleEventBus.class);
        // Event Store
        newInstance(EventStore , FileSystemEventStore.class);
        // Repository
        newInstance(Repository, EventSourcingRepository.class);

        // link repo to event bus and event store 
        newReference("subscribe_repo").from(Repository).to(Aggregate_Roots);
        newReference("subscribe_repo").from(Repository).to(EventStore);
        newReference("subscribe_repo").from(Repository).to(EventBus);
        
        // Command Handling
        newInstance(CommandHandler, AggregateAnnotationCommandHandler.class);
        newReference("subscribe").from(CommandHandler).to(Aggregate_Roots);
        newReference("subscribe").from(CommandHandler).to(Repository);
        newReference("subscribe").from(CommandHandler).to(CommandBus);
        
        // =====================================================
        
        newInstance("atLeastAMethodAnnotatedWith_@EventHandler", EventHandler.class); // at a method annotated with
        newReference().from(EventBus).to("atLeastAMethodAnnotatedWith_@EventHandler");
        
        
    }

}
