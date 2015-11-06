void addSyllabus(String syllabus,Bubble[] bubbles)  {
   //Split each syllabo
   String[] syllabo = syllabus.split("/");
   //record how many phonemes are in each syllabo
   int[] phslength=new int[syllabo.length];
   //record the past phonemes number
   int past=0;
   
    for (int i=0; i<syllabo.length; i++) {
      String[] phs=syllabo[i].split("-");
      phslength[i]=phs.length;
      for (int j=1; j<phs.length; j++)
        bubbles[past+j].adjustDistance(-20*j); 
      past+=phslength[i];
    }
}

void addStress(String stresses,String syllabus, Bubble[] bubbles ){
   String[] stress=stresses.split("/");
  //Split each syllabo
   String[] syllabo = syllabus.split("/");
   //record how many phonemes are in each syllabo
   int[] phslength=new int[syllabo.length];
   //record the past phonemes number
   int past=0;
   
   for (int i=0; i<stress.length; i++) {
     int ss=Integer.parseInt(stress[i]);
     String[] phs=syllabo[i].split("-");
     phslength[i]=phs.length;
     //if the syllabo is stressed, increase the size of the corresponding bubbles
     if(ss==1){
        for (int j=0; j<phs.length; j++)
            bubbles[past+j].increaseR();
     }
     past+=phslength[i];
   }
}