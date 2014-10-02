package com.aspnetconnect;
import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Long Serializer to avoid losing precision when sending data to Mobile
 * Services
 */
class LongSerializer implements JsonSerializer<Long> {

	/**
	 * Serializes a Long instance to a JsonElement, verifying the maximum and
	 * minimum allowed values
	 */
	@Override
	public JsonElement serialize(Long element, Type type,
			JsonSerializationContext ctx) {
		Long maxAllowedValue = 0x0020000000000000L;
		Long minAllowedValue = Long.valueOf(0xFFE0000000000000L);
		if (element != null) {
			if (element > maxAllowedValue || element < minAllowedValue) {
				throw new IllegalArgumentException(
						"Long value must be between " + minAllowedValue
								+ " and " + maxAllowedValue);
			} else {
				return new JsonPrimitive(element);
			}
		} else {
			return JsonNull.INSTANCE;
		}

	}

}
