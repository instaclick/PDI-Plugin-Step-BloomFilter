package net.nationalfibre.filter;

/**
 * Filter configuration
 *
 * @author Fabio B. Silva <fabios@nationalfibre.net>
 */
public class FilterConfig
{
    /**
     * Expected number of elements
     */
    private int expectedNumberOfElements = 1000;

    /**
     * False positive probability
     */
    private double falsePositiveProbability = 0.1;

    /**
     * URI to store filters
     */
    private String uri;

    /**
     * Number that that the epoch timestamp will be divided
     */
    private double timeDivision = 60;

    /**
     * Number of times to lookups into files
     */
    private int numberOfLookups = 1440;

    /**
     * Filter type
     */
    private FilterType filter = FilterType.BLOMM;

    /**
     * Provider type
     */
    private ProviderType provider = ProviderType.VFS;

    /**
     * Gets {@link expectedNumberOfElements}
     * @return
     */
    public int getExpectedNumberOfElements()
    {
        return expectedNumberOfElements;
    }

    public FilterConfig withExpectedNumberOfElements(int expectedNumberOfElements)
    {
        this.expectedNumberOfElements = expectedNumberOfElements;

        return this;
    }

    /**
     * Gets {@link falsePositiveProbability}
     * @return
     */
    public double getFalsePositiveProbability() {
        return falsePositiveProbability;
    }

    /**
     * Sets {@link falsePositiveProbability}
     * @return
     */
    public FilterConfig withFalsePositiveProbability(double falsePositiveProbability)
    {
        this.falsePositiveProbability = falsePositiveProbability;

        return this;
    }

    /**
     * Gets {@link numberOfLookups}
     * @return
     */
    public int getNumberOfLookups()
    {
        return numberOfLookups;
    }

    /**
     * Sets {@link numberOfLookups}
     * @return
     */
    public FilterConfig withNumberOfLookups(int lookups)
    {
        this.numberOfLookups = lookups;

        return this;
    }

    /**
     * Gets {@link timeDivision}
     * @return
     */
    public double getTimeDivision()
    {
        return timeDivision;
    }

    /**
     * Sets {@link timeDivision}
     * @return
     */
    public FilterConfig withTimeDivision(double timeDivision)
    {
        this.timeDivision = timeDivision;

        return this;
    }

    /**
     * Gets {@link uri}
     * @return
     */
    public String getURI()
    {
        return uri;
    }

    /**
     * Sets {@link uri}
     * @return
     */
    public FilterConfig withURI(String uri)
    {
        this.uri = uri;

        return this;
    }

    /**
     * Gets {@link filter}
     * @return
     */
    public FilterType getFilter()
    {
        return filter;
    }

    /**
     * Sets {@link filter}
     * @return
     */
    public FilterConfig withFilter(FilterType filter)
    {
        this.filter = filter;

        return this;
    }

    /**
     * Gets {@link provider}
     * @return
     */
    public ProviderType getProvider()
    {
        return provider;
    }

    /**
     * Sets {@link provider}
     * @return
     */
    public FilterConfig withProvider(ProviderType provider)
    {
        this.provider = provider;

        return this;
    }

    /**
     * Creates a new {@link FilterConfig}
     * @return
     */
    public static FilterConfig create()
    {
        return new FilterConfig();
    }
}
