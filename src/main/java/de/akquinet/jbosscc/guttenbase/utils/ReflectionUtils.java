package de.akquinet.jbosscc.guttenbase.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Locale.ENGLISH;

public abstract class ReflectionUtils {
  /**
   * Changing the value of a given field.
   *
   * @param object    -- target object of injection
   * @param fieldName -- name of field whose value is to be set
   * @param value     -- object that is injected
   */
  public static void setField(final Object object, final String fieldName, final Object value) {
    setField(object, object.getClass(), fieldName, value);
  }

  /**
   * Changing the value of a given field.
   *
   * @param object    -- target object of injection
   * @param clazz     -- type of argument object
   * @param fieldName -- name of field whose value is to be set
   * @param value     -- object that is injected
   */
  public static void setField(final Object object, final Class<?> clazz, final String fieldName, final Object value) {
    try {
      final Field field = findFieldPerName(clazz, fieldName);

      if (!field.isAccessible()) {
        field.setAccessible(true);
      }

      field.set(object, value);
    } catch (final Exception e) {
      throw new IllegalArgumentException("Could not set field value: " + fieldName + ":" + e.getMessage(), e);
    }
  }

  /**
   * Get the value of a given fields on a given object via reflection.
   *
   * @param object    -- target object of field access
   * @param fieldName -- name of the field
   * @return -- the value of the represented field in object; primitive values are wrapped in an appropriate object before being
   * returned
   */
  @SuppressWarnings("unchecked")
  public static <T> T getField(final Object object, final String fieldName) {
    return (T) getField(object, object.getClass(), fieldName);
  }

  /**
   * Get the value of a given fields on a given object via reflection.
   *
   * @param object    -- target object of field access
   * @param clazz     -- type of argument object
   * @param fieldName -- name of the field
   * @return -- the value of the represented field in object; primitive values are wrapped in an appropriate object before being
   * returned
   */
  public static <T> T getField(final Object object, final Class<?> clazz, final String fieldName) {
    try {
      final Field field = findFieldPerName(clazz, fieldName);
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }

      return (T) field.get(object);
    } catch (final Exception e) {
      throw new IllegalArgumentException("Could not get field value: " + fieldName, e);
    }
  }

  private static Field findFieldPerName(final Class<?> clazz, final String name) {
    final List<Field> allFields = getAllFields(clazz);
    for (final Field field : allFields) {
      if (field.getName().equals(name)) {
        return field;
      }
    }
    throw new IllegalArgumentException("No Field with name <" + name + "> found!");
  }

  public static boolean checkArguments(final Class<?>[] parameterTypes, final Object... arguments) {
    boolean match = true;

    for (int i = 0; i < arguments.length; i++) {
      final Class<?> parameterClass = parameterTypes[i];
      final Class<?> argumentClass = arguments[i].getClass();

      if (!parameterClass.isAssignableFrom(argumentClass)) {
        final boolean isInt = parameterClass == int.class && argumentClass == Integer.class;
        final boolean isDouble = parameterClass == double.class && argumentClass == Double.class;
        final boolean isBoolean = parameterClass == boolean.class && argumentClass == Boolean.class;

        if (!isInt && !isDouble && !isBoolean) {
          match = false;
        }
      }
    }

    return match;
  }

  /**
   * Invoke a given method with given arguments on a given object via reflection.
   *
   * @param object     -- target object of invocation
   * @param methodName -- name of method to be invoked
   * @param arguments  -- arguments for method invocation
   * @return -- method object to which invocation is actually dispatched
   */
  public static Object invokeMethod(final Object object, final String methodName, final Object... arguments) {
    return invokeMethod(object, object.getClass(), methodName, arguments);
  }

  /**
   * Invoke a given method with given arguments on a given object via reflection.
   *
   * @param object     -- target object of invocation
   * @param clazz      -- type of argument object
   * @param methodName -- name of method to be invoked
   * @param arguments  -- arguments for method invocation
   * @return -- method object to which invocation is actually dispatched
   */
  public static Object invokeMethod(final Object object, final Class<?> clazz, final String methodName, final Object... arguments) {
    for (final Method declaredMethod : clazz.getDeclaredMethods()) {
      if (declaredMethod.getName().equals(methodName)) {
        final Class<?>[] parameterTypes = declaredMethod.getParameterTypes();

        if (parameterTypes.length == arguments.length) {
          final boolean match = checkArguments(parameterTypes, arguments);

          if (match) {
            return setAccessableAndInvokeMethod(object, declaredMethod, arguments);
          }
        }
      }
    }

    throw new IllegalArgumentException("Method " + methodName + ":" + arguments + " not found");
  }

  public static Object setAccessableAndInvokeMethod(final Object object, final Method declaredMethod, final Object... arguments) {
    try {
      if (!declaredMethod.isAccessible()) {
        declaredMethod.setAccessible(true);
      }

      return declaredMethod.invoke(object, arguments);
    } catch (final Exception exc) {
      throw new IllegalStateException("Error invoking method: " + declaredMethod, exc);
    }
  }

  public static List<Method> getAllMethods(Class<?> clazz) {
    final List<Method> result = new ArrayList<Method>();

    while (clazz != Object.class) {
      result.addAll(Arrays.asList(clazz.getDeclaredMethods()));
      clazz = clazz.getSuperclass();
    }

    return result;
  }

  public static List<Method> getAllAnnotatedMethods(final Class<?> clazz, final Class<? extends Annotation> annotationClass) {
    final List<Method> result = new ArrayList<Method>();

    for (final Method method : getAllMethods(clazz)) {
      if (method.isAnnotationPresent(annotationClass)) {
        result.add(method);
      }
    }

    return result;
  }

  public static List<Field> getAllFields(Class<?> clazz) {
    final List<Field> result = new ArrayList<Field>();

    while (clazz != Object.class && clazz != null) {
      result.addAll(Arrays.asList(clazz.getDeclaredFields()));
      clazz = clazz.getSuperclass();
    }

    return result;
  }

  public static List<Field> getAllAnnotatedFields(final Class<?> clazz, final Class<? extends Annotation> annotationClass) {
    final List<Field> result = new ArrayList<Field>();

    for (final Field field : getAllFields(clazz)) {
      if (field.isAnnotationPresent(annotationClass)) {
        result.add(field);
      }
    }

    return result;
  }

  /**
   * Returns a String which capitalizes the first letter of the string.
   */
  public static String capitalizePropertyName(final String name) {
    if (name == null || name.length() == 0) {
      return name;
    }

    return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
  }
}
