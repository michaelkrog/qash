package dk.apaq.shopsystem.context;

import dk.apaq.shopsystem.service.OrganisationService;

/**
 *
 */
public class DataContext {

        private OrganisationService organisationService;

	private static ThreadLocal<DataContext> threadLocal = new ThreadLocal<DataContext>();

	/**
	 * INTERNAL METHOD
	 *
	 * @param createIfDoesNotExist
	 * @return DataContext
	 */
	public static DataContext get(boolean createIfDoesNotExist)
	{
		DataContext context = threadLocal.get();
		if (createIfDoesNotExist && context == null)
		{
			context = new DataContext();
			threadLocal.set(context);
		}
		return context;
	}

	/**
	 * Checks if {@link DataContext} exists for the current thread
	 *
	 * @return {@code true} if {@link DataContext} exists for the current thread
	 */
	public static boolean exists()
	{
		return get(false) != null;
	}

	/**
	 * @return {@link Application} bound to current thread
	 */
	public static OrganisationService getService()
	{
		DataContext context = get(false);
		return context != null ? context.organisationService : null;
	}

	/**
	 * Binds the specified application to current thread.
	 *
	 * @param application
	 */
	public static void setService(OrganisationService service)
	{
		DataContext context = get(true);
		context.organisationService = service;
	}



	/**
	 * Cleans the {@link DataContext} and returns previous context.
	 *
	 * @return old {@link DataContext}
	 */
	public static DataContext detach()
	{
		DataContext value = threadLocal.get();
		threadLocal.remove();
		return value;
	}

	/**
	 * Restores the context
	 *
	 * @param dataContext
	 * @see #detach()
	 */
	public static void restore(DataContext dataContext)
	{
		threadLocal.set(dataContext);
	}

	/**
	 * Construct.
	 */
	private DataContext()
	{
	}

}