package com.leekwars.generator.test;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.leekwars.generator.Log;

import leekscript.compiler.AIFile;
import leekscript.compiler.resolver.Resolver;
import leekscript.compiler.resolver.ResolverContext;

public class LocalDbResolver implements Resolver<DbContext> {

	private static final String TAG = LocalDbResolver.class.getSimpleName();

	private static class AIFolder {
		public int id;
		// public String name;
		public int folder;

		public AIFolder(int id, String name, int folder) {
			this.id = id;
			// this.name = name;
			this.folder = folder;
		}
	}

	@Override
	public AIFile<DbContext> resolve(String path, ResolverContext basecontext) throws FileNotFoundException {

		DbContext context = (DbContext) basecontext;
		if (context == null) {
			Log.w(TAG, "No context, missing farmer and folder!");
			throw new FileNotFoundException();
		}

		return getAIByName(context.getFarmer(), context.getFolder(), path);
	}

	private AIFile<DbContext> getAIByName(int owner, int cwdID, String path) throws FileNotFoundException {

		AIFolder cwd;
		if (path.length() > 0 && path.charAt(0) == '/') {
			cwd = new AIFolder(0, "/", 0);
			path = path.substring(1);
		} else {
			cwd = getAIFolder(cwdID, owner);
		}
		if (cwd == null)
			throw new FileNotFoundException();

		// Certaines anciennes IA ont des / dans le nom, ils ont été remplacés par \/
		// On ne fait donc pas un split classique, les \/ sont ignorés
		int j = 0;
		for (int i = 1; i < path.length(); ++i) {
			if (path.charAt(i) == '/' && path.charAt(i - 1) != '\\') {
				String part = path.substring(j, i).replaceAll("\\\\/", "/"); // On convertit les \/ en / à nouveau
				j = i + 1;
				cwd = findAIFolder(part, cwd.id, cwd.folder, owner);
				if (cwd == null)
					throw new FileNotFoundException();
			}
		}
		String name = path.substring(j).replaceAll("\\\\/", "/"); // On convertit les \/ en / à nouveau

		try {
			PreparedStatement statement = LocalDB.getDB().prepareStatement("SELECT * FROM ai WHERE owner=? AND name=? AND deleted = 0 AND folder=? ORDER BY id LIMIT 1");
			statement.setInt(1, owner);
			statement.setString(2, name);
			statement.setInt(3, cwd.id);
			// System.out.println("cwd " + cwd.id + " name=" + name);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				DbContext newContext = new DbContext(owner, cwd.id);
				return new AIFile<DbContext>(name, result.getString("code"), result.getLong("modified"), result.getInt("version"), newContext, result.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new FileNotFoundException();
	}

	public AIFolder findAIFolder(String name, int cwd, int parent, int owner) {
		// System.out.println("findAIFolder " + name + " " + cwd + " " + parent + " " + owner);
		if (name.equals("..")) {
			return getAIFolder(parent, owner);
		}
		try {
			PreparedStatement statement = LocalDB.getDB().prepareStatement("SELECT * FROM ai_folder WHERE deleted = 0 AND name = ? AND folder = ? AND owner = ?");
			statement.setString(1, name);
			statement.setInt(2, cwd);
			statement.setInt(3, owner);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				AIFolder ai = new AIFolder(result.getInt("id"),
						result.getString("name"), result.getInt("folder"));
				result.close();
				return ai;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public AIFolder getAIFolder(int id, int owner) {
		if (id == 0) {
			return new AIFolder(0, "/", 0);
		}
		try {
			PreparedStatement statement = LocalDB.getDB().prepareStatement("SELECT * FROM ai_folder WHERE deleted = 0 AND id = ? AND owner = ?");
			statement.setInt(1, id);
			statement.setInt(2, owner);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				AIFolder ai = new AIFolder(result.getInt("id"), result.getString("name"), result.getInt("folder"));
				result.close();
				return ai;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResolverContext createContext(int farmer, int owner, int folder) {
		return new DbContext(farmer, folder);
	}
}