package de.ckl.testing.domain.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class RangeDeterminator implements IRangeDeterminator
{
	Logger log = Logger.getLogger(RangeDeterminator.class);

	private List<IRangeDeterminator> listRangeDeterminator = new ArrayList<IRangeDeterminator>();

	@Override
	public long getMaxLength(Field _f)
	{
		List<Long> listDeterminedValues = new ArrayList<Long>();

		for (IRangeDeterminator rangeDeterminator : listRangeDeterminator)
		{
			long determinedRange = rangeDeterminator.getMaxLength(_f);
			String clazzName = rangeDeterminator.getClass().getName();

			log.info("Range determinator [" + clazzName + "] resolved ["
					+ _f.getName() + "] max length to [" + determinedRange
					+ "]");

			listDeterminedValues.add(determinedRange);
		}

		long r = -1;

		// minimum value is relevant
		for (long i : listDeterminedValues)
		{
			if (r == -1 || ((i < r) && (i != 0)))
			{
				r = i;
			}
		}

		log.info("Max value for [" + _f.getName() + "] is [" + r + "]");

		return r;
	}

	@Override
	public long getMinLength(Field _f)
	{
		List<Long> listDeterminedValues = new ArrayList<Long>();

		for (IRangeDeterminator rangeDeterminator : listRangeDeterminator)
		{
			long determinedRange = rangeDeterminator.getMinLength(_f);
			String clazzName = rangeDeterminator.getClass().getName();

			log.info("Range determinator [" + clazzName + "] resolved ["
					+ _f.getName() + "] min length to [" + determinedRange
					+ "]");

			listDeterminedValues.add(determinedRange);
		}

		long r = -1;

		// maximum value is relevant
		for (long i : listDeterminedValues)
		{
			if (r == -1 || i > r)
			{
				r = i;
			}
		}

		log.info("Min value for [" + _f.getName() + "] is [" + r + "]");

		return r;
	}

	public void setListRangeDeterminator(
			List<IRangeDeterminator> listRangeDeterminator)
	{
		this.listRangeDeterminator = listRangeDeterminator;
	}

	public List<IRangeDeterminator> getListRangeDeterminator()
	{
		return listRangeDeterminator;
	}

}
