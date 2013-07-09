package net.nationalfibre.filter;


public class Main {

	public static void main(String[] args) {
		FilterConfig config = FilterConfig.create()
			.withExpectedNumberOfElements(100)
			.withFalsePositiveProbability(0.1)
			.withProvider(ProviderType.MEMORY)
			.withNumberOfLookups(100)
			.withTimeDivision(60);

		DataFilter filter = FilterFactory.createFilter(config);

		System.out.println(filter.add(new Data("1", 1373054936L)));
	}

}
