package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyGdxGame extends ApplicationAdapter{
        
        private static final int FRAME_COLS = 6, FRAME_ROWS = 5;
    
        private final float UPDATE_TIME = 1/60f;
        float timer;
    
        //multiplayer variable
        private Socket socket;
        boolean connected = false;
    
        private int displayW;
        private int displayH;
    
        //variable to store the TileMap
	TiledMap tiledMap;
        
        //Top-Down Camera
        OrthographicCamera camera;
        //Input Class
        Control control;
        //variable to keep the TileMap Renderer in
        TiledMapRenderer tiledMapRenderer;
        
        SpriteBatch batch;
        Hero hero;
        HashMap<String, Hero> partnerPlayers; 
        
        
	@Override
	public void create () {
            Media.load_assets();
            batch = new SpriteBatch();
            
            //display Width
            displayW = Gdx.graphics.getWidth();
            //display Height 
            displayH = Gdx.graphics.getHeight();
            
            //position variables (for viewport)
            int h = (int) (displayH/Math.floor(displayH/160));
            int w = (int) (displayW/(displayH/(displayH/Math.floor(displayH/160))));         
            
            //initialize Camera with viewport size variables)
            camera = new OrthographicCamera(w,h);          
            camera.update();
            
            tiledMap = new TmxMapLoader().load("testing.tmx");
            tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
            
            control = new Control(displayW, displayH, camera);
            Gdx.input.setInputProcessor(control);
            
            hero = new Hero(new Vector2(100,100));
            partnerPlayers = new HashMap<String, Hero>();
            
            connectSocket();
            configSocketEvents();
	}

	@Override
	public void render () {
                updateServer(Gdx.graphics.getDeltaTime());
            
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            
            
                hero.update(control);
                camera.position.lerp(hero.pos3, .1f);
                
                camera.update();
            
                tiledMapRenderer.setView(camera);
                tiledMapRenderer.render();
                
                batch.setProjectionMatrix(camera.combined);
                batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                
     
                
                batch.begin();
                hero.draw(batch);
                
                for (HashMap.Entry<String, Hero> entry : partnerPlayers.entrySet()) {
                    entry.getValue().draw(batch);
                }
                
                batch.end();
	}
        
        public void updateServer(float dt) {
            timer += dt;
            if (timer >= UPDATE_TIME) {
                JSONObject data = new JSONObject();
                try {
                    data.put("x", hero.pos.x);
                    data.put("y", hero.pos.y);
                    socket.emit("playerMoved", data);
                } catch (JSONException e) {
                    Gdx.app.log("Socket.IO: ", "Error sending update data");
                }
            }
        }
        
        public void connectSocket( ) {
            
            try {
                
                //attempt to connect to public ip on port 3000
                socket = IO.socket("http://10.0.0.185:3000");
                socket.connect();
                
                
            } catch (Exception e) {
                System.out.println(e);
            }
            
        }
        
        public void configSocketEvents() {
            
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() { //when connected
                @Override
                public void call(Object...args) {
                    Gdx.app.log("SocketIO", "Connected");
                    connected = true;
                }
            }).on("socketID", new Emitter.Listener() { //when ID is recieved
               @Override
                public void call(Object...args) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String id = data.getString("id");
                        Gdx.app.log("SocketIO", "My ID: " + id);
                    } catch (JSONException e) {
                        Gdx.app.log("SocketIO", "Error getting ID.");
                    }
                    
                } 
            }).on("newPlayer", new Emitter.Listener() { //when the new player joins
                
                @Override
                public void call(Object...args) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String id = data.getString("id");
                        Gdx.app.log("SocketIO", "New Player Connected: " + id);
                        partnerPlayers.put(id, new Hero(new Vector2(100,100)));
                    } catch (JSONException e) {
                        Gdx.app.log("SocketIO", "Error getting New Player ID.");
                    }
                    
                } 
                
            }).on("playerDisconnect", new Emitter.Listener() { //when player disconnects
                
                @Override
                public void call(Object...args) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String id = data.getString("id");
                        partnerPlayers.remove(id);
                    } catch (JSONException e) {
                        Gdx.app.log("SocketIO", "Error getting New Player ID.");
                    }
                    
                } 
                
            }).on("getPlayers", new Emitter.Listener() {
                
                @Override
                public void call(Object...args) {
                    JSONArray objects = (JSONArray) args[0];
                    
                    try {
                        
                        for (int i = 0; i < objects.length(); i++) {
                            Vector2 position = new Vector2();
                            position.x = (float) objects.getJSONObject(i).getDouble("x");
                            position.y = (float) objects.getJSONObject(i).getDouble("y");
                            Hero partner = new Hero(position);
                            
                            String pId = objects.getJSONObject(i).getString("id");
                            partnerPlayers.put(pId, partner);
                        }
                        
                    } catch (JSONException e) {
                        Gdx.app.log("SocketIO", "Error getting New Player ID.");
                    }
                    
                } 
                
            }).on("playerMoved", new Emitter.Listener() { //when player disconnects
                
                @Override
                public void call(Object...args) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String id = data.getString("id");
                        Double x = data.getDouble("x");
                        Double y = data.getDouble("y");
                        if (partnerPlayers.get(id) != null) {
                            Hero player = partnerPlayers.get(id);
                            player.pos.x = x.floatValue();
                            player.pos.y = y.floatValue();
                        }
                    } catch (JSONException e) {
                        Gdx.app.log("SocketIO", "Error getting New Player ID.");
                    }
                    
                } 
                
            });
            
        }
    
        
}
