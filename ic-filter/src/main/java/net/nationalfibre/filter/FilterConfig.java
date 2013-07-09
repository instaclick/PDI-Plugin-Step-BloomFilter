package net.nationalfibre.filter;

public class FilterConfig {

	private int expectedNumberOfElements 	= 1000;
	private double falsePositiveProbability = 0.1;

	private String uri;
	private double timeDivision	= 60;
	private int numberOfLookups = 1440;
	private FilterType filter	= FilterType.BLOMM;
	private ProviderType provider= ProviderType.VFS;

	public int getExpectedNumberOfElements() {
		return expectedNumberOfElements;
	}

	public FilterConfig withExpectedNumberOfElements(int expectedNumberOfElements) {
		this.expectedNumberOfElements = expectedNumberOfElements;

		return this;
	}

	public double getFalsePositiveProbability() {
		return falsePositiveProbability;
	}

	public FilterConfig withFalsePositiveProbability(double falsePositiveProbability) {
		this.falsePositiveProbability = falsePositiveProbability;

		return this;
	}

	public int getNumberOfLookups() {
		return numberOfLookups;
	}

	public FilterConfig withNumberOfLookups(int lookups) {
		this.numberOfLookups = lookups;

		return this;
	}

	public double getTimeDivision() {
		return timeDivision;
	}

	public FilterConfig withTimeDivision(double timeDivision) {
		this.timeDivision = timeDivision;

		return this;
	}

	public String getURI() {
		return uri;
	}

	public FilterConfig withURI(String uri) {
		this.uri = uri;

		return this;
	}
	
	public FilterType getFilter() {
		return filter;
	}

	public FilterConfig withFilter(FilterType filter) {
		this.filter = filter;

		return this;
	}

	public ProviderType getProvider() {
		return provider;
	}

	public FilterConfig withProvider(ProviderType provider) {
		this.provider = provider;

		return this;
	}

	public static FilterConfig create() {
		return new FilterConfig();
	}
}
