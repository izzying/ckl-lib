package de.ckl.testing.domain;

import java.util.Date;

import de.ckl.testing.domain.generator.DateGenerator;
import de.ckl.testing.domain.generator.IntGenerator;
import de.ckl.testing.domain.generator.LongGenerator;
import de.ckl.testing.domain.generator.StringGenerator;
import de.ckl.testing.domain.handlers.JpaAnnotatedDomain;
import de.ckl.testing.domain.handlers.RangeDeterminator;
import de.ckl.testing.domain.handlers.SpringValidationAnnotatedDomain;

/**
 * Simple template which enables all available generators by default.
 * 
 * @author ckl
 * 
 * @param <E>
 */
public class DomainPropertyRandomizerTemplate<E> extends
		DomainPropertyRandomizer<E>
{
	public DomainPropertyRandomizerTemplate(Class<E> _e)
	{
		super(_e, new DataGenerationDelegator<E>());

		getDataGenerationDelegator().getMapGeneratorClasses().put(String.class,
				new StringGenerator());
		getDataGenerationDelegator().getMapGeneratorClasses().put(Long.class,
				new LongGenerator());
		getDataGenerationDelegator().getMapGeneratorClasses().put(
				Integer.class, new IntGenerator());
		getDataGenerationDelegator().getMapGeneratorClasses().put(Date.class,
				new DateGenerator());

		RangeDeterminator rangeDeterminator = new RangeDeterminator();
		rangeDeterminator.getListRangeDeterminator().add(
				new JpaAnnotatedDomain());
		rangeDeterminator.getListRangeDeterminator().add(
				new SpringValidationAnnotatedDomain());
		getDataGenerationDelegator().setRangeDeterminator(rangeDeterminator);
	}

	public static <E> DomainPropertyRandomizer<E> getInstance(Class<E> _e)
	{
		return new DomainPropertyRandomizerTemplate<E>(_e);
	}
}
