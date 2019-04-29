class Car{
  CircleAgent wheel1, wheel2;
  PolygonAgent base;
  Joint joint1, joint2;
  
  Car(float x, float y){
    ArrayList<Vec2> basePoints = new ArrayList();
    basePoints.add(new Vec2(-25,-3));
    basePoints.add(new Vec2(25,-3));
    basePoints.add(new Vec2(25,3));
    basePoints.add(new Vec2(-25,3));
    base = new PolygonAgent(x, y, basePoints, BodyType.DYNAMIC);
    
    wheel1 = new CircleAgent(x-25,y,15);
    wheel2 = new CircleAgent(x+25,y,15);
    
    RevoluteJointDef rjd1 = new RevoluteJointDef();
    RevoluteJointDef rjd2 = new RevoluteJointDef();
    rjd1.initialize(base.body,wheel1.body,wheel1.body.getTransform().p);
    rjd2.initialize(base.body,wheel2.body,wheel2.body.getTransform().p);
    
    float torque = random(-8000,-6000);
    float speed = random(5000,10000);

    rjd1.motorSpeed = -PI * speed;
    rjd1.maxMotorTorque = torque;
    rjd1.enableMotor = true;
    
    rjd2.motorSpeed = -PI * speed;
    rjd2.maxMotorTorque = torque;
    rjd2.enableMotor = true;
    
    joint1 = box2d.createJoint(rjd1);
    joint2 = box2d.createJoint(rjd2);
  }
  
  void display(){
    base.display();
    wheel1.display();
    wheel2.display();
  }  
  
  boolean done(){
    if (wheel1.done() && wheel2.done()) return true;
    else return false;
  }
}
