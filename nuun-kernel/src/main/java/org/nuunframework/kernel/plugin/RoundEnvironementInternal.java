package org.nuunframework.kernel.plugin;

public class RoundEnvironementInternal implements RoundEnvironment
{

    private int roundNumber = 0;
    
    public RoundEnvironementInternal()
    {
    }    
    
    @Override
    public int roundNumber()
    {
        return this.roundNumber;
    }
    
    
    public void incrementRoundNumber()
    {
        this.roundNumber++;
    }

    @Override
    public boolean firstRound()
    {
        return this.roundNumber == 0;
    }

}
