import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.concurrent.*;

class Playing
{
static boolean P_flag[]=new boolean[9];
static boolean AI_flag[]=new boolean[9];
static boolean whosturn=true;
static boolean wait;
static JLabel playing_details=new JLabel();
static boolean whowillstart=false; //use this for ai only... To decide who should start the game!!
static boolean whohaswon=true;

 //To check for the first turn
 static boolean isthisthefirstturn=true;

 //Constructor... To provide add Click process
 Playing()
 {
	//adding playing title under ai
	playing_details.setText("Thinking...");
	playing_details.setForeground(Color.yellow);
	playing_details.setBounds(350,110,70,30);
	Design.jp.add(playing_details);
	playing_details.setVisible(false);

	
	 //To check for the first turn
	 isthisthefirstturn=true;
	 whohaswon=true;

	 // To Set who'll start the match!!
	if(whowillstart)
	   { whowillstart=false; wait=false; whosturn=false; }
	else
	   { whowillstart=true; wait=true; whosturn=true;}
	   

	//Initialising the P_flags and AI_flags
	for(int i=0;i<9;i++)
	{
		P_flag[i]=false; // for no values
		AI_flag[i]=false;// for no values
	}

	Design.Board.addMouseListener(new MouseAdapter(){
	public void mouseClicked(MouseEvent e)
	  {
		int x=e.getX()+0; int y=e.getY()+0;
		int pos=Design.sensor(x,y);

	    if(!Design.Pos_status[pos]) //To check for unfilled spaces
	    if(whosturn) //To check for who's turn it is
		{
		//testing
		System.out.println("Clicked");
		Design.Pos[pos].setBackground(Color.blue);
		P_flag[pos]=true; // For setting values to Panels:
		Design.Pos_status[pos]=true;
		Design.Pos[pos].setVisible(true);
		whosturn=false; // passing turn to ai
		wait=false; // To start ai when it's not it's turn!
		}
 	  }
	});
 }
 public static void ask_ai_Toplay()
 {
	while(true) {
	System.out.println("AI STARTED WORKING"); if(!wait){ whosturn=false; break; }}
	while(true)
	{ System.out.println("Ai working"+whosturn);
	 if(!whosturn) //checking for ai turn
	 { ai_playing(); }
	if(!check_for_game_ending()){System.out.println("Game ended"); break; }
	}
 }
//============================== ai methods =======================================//
 public static int method3()
 {

	int p;
	boolean q=true;
	     do{
		q=true;
		p=(int)(Math.random()*10)%9;
		if(Design.Pos_status[p])
			q=false;
		}while(!q);
	return p;	
 }
//============================== ai methods =======================================//
 public static int ai_methods()
 {
	///check if any way has been selected or not!!
	boolean found=false; //-----------------------> true means found a way
	int value=-1; // ------------> what to return !!

	//if the ai is playing for first time!!
	if(isthisthefirstturn)
/*Here*/{
		isthisthefirstturn=false;
		String data_collection="";
		
		//Opening file to check how many ways are there to start!!
			int count_of_ways=0; //----------> to count how many ways are there to play
			File file=new File("AI_Diary_1.txt");
			try{
				//To count numbers of line and store the first letters
				if(!file.exists()) file.createNewFile();
				    FileReader fr=new FileReader(file);
				    BufferedReader br=new BufferedReader(fr);
				    StringBuffer sb=new StringBuffer();
				    String line;
				    while((line=br.readLine())!=null)
					if(line.length()!=0)
					if(!Design.Pos_status[line.charAt(0)-48])
				 	  {
						boolean Flg=true;
						for(int i=0;i<data_collection.length();i++)
						   if(line.charAt(0)==data_collection.charAt(i))
							{ Flg=false; break; }
						if(Flg)
						  { data_collection+=line.charAt(0); count_of_ways++; }
					  }
				    fr.close();
			   }catch(Exception e){ }

		if(count_of_ways!=0)
		{
		//Now select which method to play!! ---> choose randomly!!
			int len=data_collection.length();
			int temp=len*2;
			while(true)
			{
				len=(int)(Math.random()*10)%len;
				value=(int)data_collection.charAt(len)-48;
				System.out.println("loop1");
				temp--;
				if(!AI_flag[value]) break;
				if(temp==0)
				{
				  while(true){
						 value=(int)(Math.random()*10)%9; 
						if(!Design.Pos_status[value])
							 break; 
					     }
				 break;
				}
			}
			found=true;  System.out.println("at firstchance :"+value);
			//JOptionPane.showMessageDialog(new JFrame(),"First Chance");
			return value;			
		}else
		{
			while(true)
			{
				value=(int)(Math.random()*10)%9;
				System.out.println("loop2");
				if(!AI_flag[value]) break;
			}
			found=true; System.out.println(" method3 at firstchance :"+value);
			//JOptionPane.showMessageDialog(new JFrame(),"First Chance but no chance found");
			return value;
		}
/*Here*/  }
	else
	{
		//Check for win: -----------------------------> 
		{
			String chances="";
			String ai_his="";
			for(int i=0;i<9;i++) if(AI_flag[i]) ai_his+=i;

			File file=new File("AI_Diary_1.txt");
			try{
				if(!file.exists()) file.createNewFile();
				    FileReader fr=new FileReader(file);
				    BufferedReader br=new BufferedReader(fr);
				    StringBuffer sb=new StringBuffer();
				    String line;
				    while((line=br.readLine())!=null)
				     if(ai_his.length()+1==line.length())
					{
						//check for presence!!
						{
							boolean FL[]=new boolean[ai_his.length()];
							boolean allow=true;
							for(int i=0;i<ai_his.length();i++) FL[i]=false;

							for(int i=0;i<ai_his.length();i++)
							for(int j=0;j<line.length();j++)
							if(ai_his.charAt(i)==line.charAt(j))
								FL[i]=true;
				
							//if all found!!
							for(int i=0;i<FL.length;i++) 
								if(!FL[i]) { allow=false; break; }
							
							//all found then check for storing values!!!
							if(allow)
							for(int i=0;i<line.length();i++)
							{
								boolean f=true;
								for(int j=0;j<ai_his.length();j++)
									if(line.charAt(i)==ai_his.charAt(j))
										{ f=false; j=line.length(); }
								if(f) chances+=line.charAt(i);
							}
							//removing repeatation:
							if(allow)
							{
							 	 String T="";
							 	for(int i=0;i<chances.length()-1;i++)
								{
								 boolean Q=true;
							 	 for(int j=i+1;j<chances.length();j++)
									if(chances.charAt(i)==chances.charAt(j)) 
										Q=false;
								 if(Q) T+=chances.charAt(i);
								}
							  chances=T;
							}
						}
					}	
				    fr.close();
			}catch(Exception e){ }
			if(chances.length()>0){
				value=(int)(Math.random()*100)%chances.length();
				value=(int)chances.charAt(value)-48;
				found=true;
			//JOptionPane.showMessageDialog(new JFrame(),"Winning Chance");
				return value;
				}			
		}

	//for draw
	{
		String data_collection="";
		String chances="";
		//read player's plan
		for(int i=0;i<9;i++)
			if(P_flag[i])
				data_collection+=i;
		
		//now collect the information from the file to stop player from winning!!
		if(data_collection.length()!=0)
		{
			File file=new File("AI_Diary_1.txt");
			try{
				if(!file.exists()) file.createNewFile();
				    FileReader fr=new FileReader(file);
				    BufferedReader br=new BufferedReader(fr);
				    StringBuffer sb=new StringBuffer();
				    String line;
				    while((line=br.readLine())!=null)
				 	{ 
						int c=0; //---> count for presence
						
						//check whether it can be used or not!!
						for(int i=0;i<data_collection.length();i++)
						for(int j=0;j<line.length();j++)
							if(data_collection.charAt(i)==line.charAt(j))
							{ c++; j=line.length(); }
						
						if(c==data_collection.length())
						{
							for(int i=0;i<line.length();i++)
							{
								boolean Fl=true;
							 for(int j=0;j<data_collection.length();j++)
								if(line.charAt(i)==data_collection.charAt(j))
								Fl=false;

							if(Fl)if(!Design.Pos_status[line.charAt(i)-48]) chances+=line.charAt(i);
							}
						}	
					}
				    fr.close();
			   }catch(Exception e){ }

			if(chances.length()!=0)
			 {
				int check=1000;
				while(true)
				{
				  int tem=(int)(Math.random()*100)%chances.length();
				  value=(int)chances.charAt(tem)-48;
			          System.out.println("loop3");
				  if(!AI_flag[value]) break;
			        }
				if(check!=0)
				found=true; 
			//JOptionPane.showMessageDialog(new JFrame(),"Draw Chance"+value);
				return value;
		        }
		}
	}
	}
	if(!found){ value=method3(); System.out.println("At method3 :"+value); }
			//JOptionPane.showMessageDialog(new JFrame(),"Random Chance"); 
	return value;
 }
 public static void ai_playing()
 {
	if(check_for_game_ending()) //Checking before ai plays
	{
	playing_details.setVisible(true);
	try{Thread.sleep(1000);}catch(Exception ex){}
	int p=ai_methods();
	System.out.println(p);
	Design.Pos[p].setBackground(Color.green);
	whosturn=true;
	AI_flag[p]=true;
	Design.Pos_status[p]=true;	
	check_for_game_ending(); //Checking after ai plays
	playing_details.setVisible(false);
	}
 }
 public static boolean check_for_game_ending()
 {
	int data=-2;
	boolean F=true;
	//checking for player's win:
	if((P_flag[0]&&P_flag[1]&&P_flag[2])||(P_flag[3]&&P_flag[4]&&P_flag[5])||(P_flag[6]&&P_flag[7]&&P_flag[8])||(P_flag[0]&&P_flag[4]&&P_flag[8])||(P_flag[2]&&P_flag[4]&&P_flag[6])||(P_flag[0]&&P_flag[3]&&P_flag[6])||(P_flag[1]&&P_flag[4]&&P_flag[7])||(P_flag[2]&&P_flag[5]&&P_flag[8]))
	data=1;
	//checking for ai's win:
	else if((AI_flag[0]&&AI_flag[1]&&AI_flag[2])||(AI_flag[3]&&AI_flag[4]&&AI_flag[5])||(AI_flag[6]&&AI_flag[7]&&AI_flag[8])||(AI_flag[0]&&AI_flag[4]&&AI_flag[8])||(AI_flag[2]&&AI_flag[4]&&AI_flag[6])||(AI_flag[0]&&AI_flag[3]&&AI_flag[6])||(AI_flag[1]&&AI_flag[4]&&AI_flag[7])||(AI_flag[2]&&AI_flag[5]&&AI_flag[8]))
	data=-1;
	//or put as draw
	else{
		int T=0;
		 for(int i=0;i<9;i++)
		  if(Design.Pos_status[i])
		   T++; 	
		if(T==9) data=0;
	     }

	if(data==1){
		JOptionPane.showMessageDialog(new JFrame(),"You Won!!");
		Design.score_player++;
		F=false;
		whohaswon=true; //to denote player has won
		write_into_file(data);
		Design.updatescore();
		Design.reset();
		}
	else if(data==-1){
		JOptionPane.showMessageDialog(new JFrame(),"You Lost!!");
		Design.score_ai++;
		F=false;
		whohaswon=false; // to denote ai has won
		write_into_file(1);
		Design.updatescore();
		Design.reset();
		}
	else if(data==0){
		JOptionPane.showMessageDialog(new JFrame(),"Draw");
		F=false;
		write_into_file(data);
		Design.updatescore();
		Design.reset();
		}
	return F;
 }
 public static void write_into_file(int i)
 {
	File file;
	String DATA="";
        boolean flag=false;
	switch(i)
	{
	case 0:
			for(int j=0;j<9;j++) if(Design.Pos_status[j]) DATA+=j;
			file=new File("AI_Diary_0.txt");
				try{
				    if(!file.exists()) file.createNewFile();
				    FileReader fr=new FileReader(file);
				    BufferedReader br=new BufferedReader(fr);
				    StringBuffer sb=new StringBuffer();
				    String line;
					while((line=br.readLine())!=null)
					  if(line.equals(DATA))
						{ flag=true; break; }
					fr.close();
				   }catch(Exception z){ }
				try{
				if(!file.exists()) file.createNewFile();
				FileWriter fw=new FileWriter(file,true);
				BufferedWriter bw=new BufferedWriter(fw);
				bw.write(DATA+"\n");
				bw.close();
				 }catch(Exception e){ }
			break;
	case 1:
			for(int j=0;j<9;j++) 
				if(whohaswon)
					{ if(P_flag[j]) DATA+=j; }
				else { if(AI_flag[j]) DATA+=j; }
			
			file=new File("AI_Diary_1.txt");
				try{
				    if(!file.exists()) file.createNewFile();
				    FileReader fr=new FileReader(file);
				    BufferedReader br=new BufferedReader(fr);
				    StringBuffer sb=new StringBuffer();
				    String line;
					while((line=br.readLine())!=null)
					  if(line.equals(DATA))
						{ flag=true; break; }
					fr.close();
				   }catch(Exception z){ }
				try{
				if(!file.exists()) file.createNewFile();
				FileWriter fw=new FileWriter(file,true);
				BufferedWriter bw=new BufferedWriter(fw);
				bw.write(DATA+"\n");
				bw.close();
				 }catch(Exception e){ }
			break;
		
	}	
 }
 
}

class Design
{
JFrame jf;
static JPanel jp;
static JPanel Board;
static JLabel player1,player2;
static JPanel Pos[]=new JPanel[9]; //---------> small 9 boxes which glows blue or green
static boolean Pos_status[]=new boolean[9]; // -----> status of the boxes
static int score_player=0;// --------->For Scores
static int score_ai=0; // -------------> For Scores
static int temp=0;
 private void addframe()
 {
	jf=new JFrame("Tic-Tac_Toe");
	jf.setVisible(true);
	jf.setLayout(null);
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jf.setSize(500,500);
	jf.setResizable(false);
	jf.setIconImage((Toolkit.getDefaultToolkit().getImage("Icon.png")));
	jf.setLocation(150,150);
 }
 public void refreshframe()
 {
	jf.setVisible(false);
	jf.setVisible(true);
 }
 private void addJPanel()
 {
	jp=new JPanel();
	jp.setBounds(0,0,500,500);
	jp.setLayout(null);
	jp.setBackground(Color.black);
	jp.setForeground(Color.red);
	jp.setVisible(true);
	jf.add(jp);
 }
 private void heading()
 {
	JLabel head=new JLabel("TIC - TOC - TOE");
	head.setFont(new Font("SimSun",Font.BOLD,25));
	head.setBounds(130,20,230,40);
	head.setForeground(Color.red);
	head.setVisible(true);
	jp.add(head);
 }
 private void setplayername()
 {
	player1=new JLabel(Intro.username+" : "+score_player);
	player2=new JLabel("A.I. : "+score_ai);
	player1.setForeground(Color.blue);
	player2.setForeground(Color.green);
	jp.add(player1);
	jp.add(player2);
	player1.setBounds(60,85,Intro.username.length()*9+15,30);
	player2.setBounds(350,85,50,30);
 }
 public static void updatescore()
 {
	player1.setText(Intro.username+" : "+score_player);
	player2.setText("A.I. : "+score_ai);
	player1.setVisible(false);
	player2.setVisible(false);
	player1.setVisible(true);
	player2.setVisible(true);
 }
 private void addBoard()
 {
	Board=new JPanel();
	jp.add(Board);
	Board.setVisible(true);
	Board.setLayout(null);
	Board.setBounds(125,200,225,225);
	Board.setBackground(Color.black);
	
	//adding hash symbol
	JPanel P[]=new JPanel[4];
	for(int i=0;i<4;i++)
	{
		P[i]=new JPanel();
		Board.add(P[i]);
		P[i].setLayout(null);
		P[i].setVisible(true);
		P[i].setBackground(Color.white);
	}
	P[0].setBounds(75,0,5,225);
	P[1].setBounds(150,0,5,225);
	P[2].setBounds(0,75,225,5);
	P[3].setBounds(0,150,225,5);
	
 }
 public static void refreshBoard()
 {
	Board.setVisible(false);
	Board.setVisible(true);
 }
 public void adddesign()
 {
	//adding location for X and Y
	int X[],Y[];
	X=new int[9];
	Y=new int[9];
	for(int i=0,j=-1;i<9;i++)
	{
		X[i]=15+(75*(i%3));
		if(i%3==0)
		X[i]+=3;

		if(i%3==0)
		j++;
		Y[i]=16+(75*j);
	
		//assing false to all small boxes
		Pos_status[i]=false;
	}
	for(int i=0;i<9;i++)
	{
		Pos[i]=new JPanel();
		Pos[i].setVisible(true);
		Pos[i].setLayout(null);
		Pos[i].setBackground(Color.black);
		Board.add(Pos[i]);
		Pos[i].setBounds(X[i],Y[i],45,45);
		Pos_status[i]=false;
		temp=i;
	}
 }
 public static int sensor(int x,int y)
 {
		int pos=0;

		if(x>0&&y>0&&x<75&&y<75) pos=0;
		else if(x>75&&x<150&&y>0&&y<75) pos=1;
		else if(x>150&&x<225&&y>0&&y<75) pos=2;
		else if(x>0&&x<75&&y>75&&y<150) pos=3;
		else if(x>75&&x<150&&y>75&&y<150) pos=4;
		else if(x>150&&x<225&&y>75&&y<150) pos=5;
		else if(x>0&&x<75&&y>150&&y<225) pos=6;
		else if(x>75&&x<150&&y>150&&y<225) pos=7;
		else if(x>150&&x<225&&y>150&&y<225) pos=8;

		return pos;
 }
 public static void reset()
 {
	for(int i=0;i<9;i++)
	{
		Pos_status[i]=false;
		Pos[i].setBackground(Color.black);
		Pos[i].setVisible(false);
		Pos[i].setVisible(true);
		Playing.P_flag[i]=false;
		Playing.AI_flag[i]=false;
	}
	new Playing().ask_ai_Toplay();	
 }
 public void put_design()
 {
	addframe();
	addJPanel();
	heading();
	setplayername();
	addBoard();
	adddesign();
	new Playing().ask_ai_Toplay();
 }
}

class Intro
{
 static String username="Ujjwal";
 JPanel P;
 public void intro()
 {
	JFrame temp=new JFrame("Introduction");
	temp.setVisible(true);
	temp.setLayout(null);
	temp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	temp.setSize(500,500);
	temp.setResizable(false);
	temp.setIconImage(Toolkit.getDefaultToolkit().getImage("Icon.png"));
	temp.setLocation(150,50);

	P=new JPanel();
	P.setVisible(true);
	P.setLayout(null);
	P.setBounds(0,0,500,500);
	P.setBackground(Color.black);
	temp.add(P);
	
	JLabel L[]=new JLabel[9];
	for(int i=0;i<9;i++)
	{
	  L[i]=new JLabel();
	  P.add(L[i]);
	  L[i].setForeground(Color.blue);
	}
	L[0].setText("Hello..  I'm  a  bot !!");
	L[1].setText("I  have  been  Created  by  Mr.  Arcon  Steve  : )");
	L[2].setText("I  can  play  this  game  myself  by  learning  the tricks  from  you.");
	L[0].setBounds(10,15,130,30);
	L[1].setBounds(30,50,300,30);
	L[2].setBounds(30,90,370,30);

	L[3].setText("I  would  like  to  know  your  name,");
	L[3].setBounds(30,130,230,30);
	L[4].setText("Your Name: ");
	L[4].setBounds(70,180,80,40);
	
	JTextField tf=new JTextField("Here");
	tf.setBackground(Color.black);
	tf.setForeground(Color.green);
	tf.setBounds(150,180,100,30);
	P.add(tf);

	JButton ok=new JButton();
	ok.setIcon(new ImageIcon("Submit.png"));
	ok.setBounds(280,180,120,30);
	ok.setVisible(false);
	ok.setVisible(true);
	P.add(ok);	

	ok.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e)
	{
	ok.setVisible(false);
	tf.setVisible(false);
	username=tf.getText();
	L[8].setForeground(Color.green);
	L[8].setText(username);
	L[8].setBounds(150,180,100,40);
	L[8].setVisible(false);
	L[8].setVisible(true);
	L[5].setText("Hello "+username);
	L[6].setText("Let's Play Game : )");
	L[7].setText("ALL THE BEST!!");
	L[5].setBounds(70,250,150,30);
	L[6].setBounds(100,300,120,30);
	L[7].setBounds(180,340,120,30);
	L[7].setForeground(Color.white);
	
	ok.setBounds(180,410,150,30);
	ok.setIcon(new ImageIcon("start.JPG"));
	ok.setVisible(false);
	ok.setVisible(true);
	ok.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e)
	{
 	 temp.dispose();
	 TTT.tf=false;
	}
	});
	}});
 }
 public void refresh()
 {
	P.setVisible(false);
	P.setVisible(true);
 }
}
public class TTT
{
 static boolean tf=true;
 public static void main(String...args)
 {
	Intro n=new Intro();
	n.intro();
	n.refresh();
	int temp=100;
	while(true){  if(temp-->0) System.out.println("Designing page!");
	if(!tf){
	 System.out.println("Page Designed!");
	 Design obj=new Design();
 	 obj.put_design();
	 Design.reset();
	 break;
	}}
 }
}