package com.movcmpret.ovpn;

/*-
 * #%L
 * ezOVPNClient
 * %%
 * Copyright (C) 2018 - 2019 movcmpret.com
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */



import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import com.movcmpret.ovpn.config.OVPNConfig;
import com.movcmpret.utility.Logger;

/**
 *  Used to let GSON differentiate between all OVPNConfigs. (Polymorphism support)
 * @author movcmpret
 *
 */
public class OVPNConfigJSONAdapter implements JsonSerializer<OVPNConfig>, JsonDeserializer<OVPNConfig> {

	public static final String Classname = "CLASSNAME";
	public static final String Instance = "INSTANCE";
	@Override
	public OVPNConfig deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonObject jsonObject = arg0.getAsJsonObject();
		JsonPrimitive prim = (JsonPrimitive)jsonObject.get(Classname);
		String classname =  prim.getAsString();
		
		Class<?> klasse = null;
		try 
		{
			klasse = Class.forName(classname);
		}
		catch(ClassNotFoundException ce )
		{
			ce.printStackTrace();
		Logger.LogError(ce.getMessage());	
		}
		catch(JsonParseException e) {
			e.printStackTrace();
			Logger.LogError(e.getMessage());
		}
		return arg2.deserialize(jsonObject.get(Instance), klasse);
	}

	@Override
	public JsonElement serialize(OVPNConfig arg0, Type arg1, JsonSerializationContext arg2) {
		JsonObject ret = new JsonObject();
		ret.addProperty(Classname, arg0.getClass().getName());
		JsonElement element = arg2.serialize(arg0);
		ret.add(Instance, element);
		return ret;
	}

}
