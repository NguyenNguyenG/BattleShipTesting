package com.example.nguyennguyen.battleshiptesting;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MyMap currentMap;
    private int COLOR = 0;
    private int CLEAR = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Ship ship = new Ship();
        final Ship enemyship = new Ship();

        final ArrayList<ArrayList<ImageButtonWrapper>> skeletonMap = new ArrayList<>();



        for(int j = 0; j < 5; j ++) {
            final LinearLayout currLayout;
            switch (j) {
                case 0:
                    currLayout = (LinearLayout) findViewById(R.id.first_row);
                    break;
                case 1:
                    currLayout = (LinearLayout) findViewById(R.id.second_row);
                    break;
                case 2:
                    currLayout = (LinearLayout) findViewById(R.id.third_row);
                    break;
                case 3:
                    currLayout = (LinearLayout) findViewById(R.id.fourth_row);
                    break;
                case 4:
                    currLayout = (LinearLayout) findViewById(R.id.fifth_row);
                    break;
                default:
                    currLayout = (LinearLayout) findViewById(R.id.fifth_row);
            }


            ArrayList<ImageButtonWrapper> currRow = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                final ImageButtonWrapper imageButtonWrapper = new ImageButtonWrapper(getApplicationContext(), i, j);
                ImageButton btn = imageButtonWrapper.getButton();
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));
                if(i == ship.getxCoordinate() && j == ship.getyCoordinate())
                    btn.setBackgroundResource(R.drawable.ship1);
                else
                    btn.setBackgroundResource(R.drawable.water);

                btn.setId(j*10 + i);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(currentMap.pickMovePhase && imageButtonWrapper.getyCoord() == ship.getyCoordinate() && imageButtonWrapper.getxCoord() == ship.getxCoordinate()) {
                            currentMap.calculateMove(ship, COLOR);
                            currentMap.pickMovePhase = false;
                            currentMap.doMovePhase = true;
                        }
                        else if(currentMap.doMovePhase){
                            if(imageButtonWrapper.getMove()) {
                                currentMap.calculateMove(ship,CLEAR);
                                skeletonMap.get(ship.getyCoordinate()).get(ship.getxCoordinate()).getButton().setBackgroundResource(R.drawable.water);

                                currentMap.move(ship,imageButtonWrapper.getxCoord(), imageButtonWrapper.getyCoord());
                                ImageButton currButton  = (ImageButton) view;
                                currButton.setBackgroundResource(R.drawable.ship1);
                                currentMap.doMovePhase = false;
                                currentMap.pickAttackPhase = true;
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Invalid Move", Toast.LENGTH_SHORT).show();
                                currentMap.calculateMove(ship, CLEAR);
                                currentMap.doMovePhase = false;
                                currentMap.pickMovePhase = true;
                            }
                        }
                        else if(currentMap.pickAttackPhase){
                            currentMap.calculateShoot(ship, COLOR);
                            currentMap.pickAttackPhase = false;
                            currentMap.doAttackPhase = true;
                        }
                        else if(currentMap.doAttackPhase) {
                            if(imageButtonWrapper.getShoot()){
                                currentMap.calculateShoot(ship , CLEAR);
                                currentMap.attack(imageButtonWrapper.getxCoord(), imageButtonWrapper.getyCoord(), ship, enemyship);


                                currentMap.doAttackPhase = false;
                                currentMap.pickMovePhase = true;
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Invalid Attack", Toast.LENGTH_SHORT).show();
                                currentMap.calculateShoot(ship, CLEAR);
                                currentMap.pickAttackPhase = true;
                                currentMap.doAttackPhase = false;
                            }

                        }
                    }
                });
                currRow.add(imageButtonWrapper);
                currLayout.addView(btn);
            }
            skeletonMap.add(currRow);

        }

        currentMap = new MyMap(getApplicationContext(), skeletonMap);
    }
}
