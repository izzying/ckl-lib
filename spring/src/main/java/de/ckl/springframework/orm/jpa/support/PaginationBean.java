package de.ckl.springframework.orm.jpa.support;

/**
 * Bean class for pagination
 * 
 * @author ckl
 * 
 */
public class PaginationBean
{
	/**
	 * offset of entries
	 */
	private int offset;

	/**
	 * size of entries
	 */
	private int size;

	/**
	 * Maximum size per list
	 */
	public final static int MAX_SIZE = 20;

	/**
	 * Sets size. If size > {@value #MAX_SIZE} or size <= 0, size is set to
	 * {@value #MAX_SIZE}
	 * 
	 * @param size
	 */
	public void setSize(int size)
	{
		if (size > MAX_SIZE || size <= 0)
		{
			size = MAX_SIZE;
		}

		this.size = size;
	}

	public int getSize()
	{
		return size;
	}

	/**
	 * If offset is < 0, offset is set to 0
	 * 
	 * @param offset
	 */
	public void setOffset(int offset)
	{
		if (offset < 0)
		{
			offset = 0;
		}

		this.offset = offset;
	}

	public int getOffset()
	{
		return offset;
	}
}
