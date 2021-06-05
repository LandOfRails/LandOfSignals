package net.landofrails.stellwand.utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import cam72cam.mod.ModCore;
import net.landofrails.stellwand.content.blocks.others.BlockSignal;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;

public class StellwandUtils {

	private StellwandUtils() {

	}

	/**
	 * 
	 * @deprecated (Will be removed with the next major update. Reason: Not used
	 *             anywhere. Don't use it.)
	 * @since LandOfSignals 0.0.3
	 * @forRemoval
	 * 
	 * @return If found; the mod folder
	 */
	@Deprecated
	@SuppressWarnings("java:S1133")
	public static Optional<File> getModFolder() {
		try {
			URL url = BlockSignal.class.getProtectionDomain().getCodeSource().getLocation();
			String uri = new URI(url.toString().replace(" ", "%20")).getSchemeSpecificPart();
			File file = new File(uri.split("!")[0]);
			file = file.getParentFile();
			return Optional.ofNullable(file);
		} catch (NullPointerException | URISyntaxException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	public static Set<Class<?>> getAllExtendedOrImplementedTypesRecursively(Class<?> clazz) {
		List<Class<?>> res = new ArrayList<>();

		do {
			res.add(clazz);

			// First, add all the interfaces implemented by this class
			Class<?>[] interfaces = clazz.getInterfaces();
			if (interfaces.length > 0) {
				res.addAll(Arrays.asList(interfaces));

				for (Class<?> interfaze : interfaces) {
					res.addAll(getAllExtendedOrImplementedTypesRecursively(interfaze));
				}
			}

			// Add the super class
			Class<?> superClass = clazz.getSuperclass();

			// Interfaces does not have java,lang.Object as superclass, they
			// have null, so break the cycle and return
			if (superClass == null) {
				break;
			}

			// Now inspect the superclass
			clazz = superClass;
			
			if("java.lang.Object".equals(clazz.getCanonicalName()))
				res.add(clazz);
			
		} while (!"java.lang.Object".equals(clazz.getCanonicalName()));

		return new HashSet<>(res);
	}

	public static void printSuperclasses(ContentPackEntryBlock cpeb) {
		ModCore.error("Superclasses:");
		if (cpeb != null)
			StellwandUtils.getAllExtendedOrImplementedTypesRecursively(cpeb.getClass()).forEach(c -> ModCore.error("\t" + c.getName()));
	}

}
