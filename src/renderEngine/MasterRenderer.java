package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blocks.Block;
import entities.EntityCamera;
import entities.EntityLight;
import entities.EntityModel;
import shaders.StaticShader;
import world.Coord3d;
import world.World;

public class MasterRenderer {
	
	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	private Map<Block, List<Coord3d>> blocks = new HashMap<Block, List<Coord3d>>();
	private List<EntityModel> entities = new ArrayList<EntityModel>();
	
	public void render(EntityLight sun, EntityCamera camera) {
		Map<Coord3d, Block> worldBlocks = World.getRenderableBlocks();
		for(Coord3d pos:worldBlocks.keySet()) {
			processBlock(pos, worldBlocks.get(pos));
		}
		entities.addAll(World.getRenderableEntities());
		renderer.prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(blocks);
		renderer.render(entities);
		shader.stop();
		blocks.clear();
	}
	
	public void processBlock(Coord3d pos, Block block) {
		if(blocks.containsKey(block)) {
			blocks.get(block).add(pos);
		} else {
			List<Coord3d> list = new ArrayList<Coord3d>();
			list.add(pos);
			blocks.put(block, list);
		}
	}
	
	public void clear() {
		shader.clear();
	}
}
