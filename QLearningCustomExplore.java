package reinforcement.qlearning;
import java.io.BufferedReader;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.util.Random;

	import reinforcement.qlearning.PolicyBean;

	public class QLearningCustomExplore {

		public static void main(String[] args) throws NumberFormatException, IOException {
			// TODO Auto-generated method stub
			int numberOfRows = 0;
			int numberOfColumns = 0;
			/******************************************** enter states *************************************/
			//System.out.println("Enter the number of states in the form of rows and columns");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			numberOfRows = 3;
			numberOfColumns=4;
			double [][] matrix=new double[numberOfRows][numberOfColumns];
			for(int i=0;i<numberOfRows;i++){
				for(int j=0;j<numberOfColumns;j++){
					matrix[i][j]=0;
				}
			}
			
			/******************************************** enter discount ************************************/
			System.out.println("Enter discount between 0 and 1");
			double discount = Double.parseDouble(br.readLine());
		/********************************************Enter the bonus for new state transitions**************/
			double bonus=0.00002;

		/******************************************making q val 12*12 matrix from 3*4 input matrix*************/	
		    double[][] qVal=new double[numberOfRows*numberOfColumns][4];
		    for(int i=0;i<qVal.length;i++){
		    	for(int j=0;j<4;j++){
		    		qVal[i][j]=0;
		    	}
		    }
			double[][] newQVal=new double[qVal.length][4];
			int[][] frequency=new int[qVal.length][4];
			double[] findMax=new double[4];
			double maximumElement=0.0;
			double max=0;
			int[] action={1,2,3,4};
			System.out.println("Q val matrix");
			for(int i=0;i<qVal.length;i++){
				for(int j=0;j<4;j++){
					newQVal[i][j]=qVal[i][j];
					frequency[i][j]=0;
					System.out.print(frequency[i][j]+" ");
//					System.out.print(qVal[i][j]+"      ");
				}
				System.out.println();
			}
			int currentState = 0;
			int currentAction=0;
			int stateAfterAction=0;
			boolean continueFlag=true;
			int[][] policyForMatrix=new int[3][4];
			int[][] oldPolicyMatrix=new int[3][4];
			int maximumValuedActionInNextState=0;
			for(int i=0;i<3;i++){
				for(int j=0;j<4;j++){
					policyForMatrix[i][j]=oldPolicyMatrix[i][j]=0;
				}
			}
			Random rand=new Random();
			int iterations=0;
			do{
				iterations++;
				for(int i=0;i<qVal.length;i++){
					if(i==5){
						continue;
					}
					continueFlag=true;
					currentState=i;
					currentAction=rand.nextInt((action.length)-1);
					while(currentState!=3 && currentState!=7){
						stateAfterAction=findNextState(currentState, currentAction);
						System.out.println("Action:"+currentAction+"in state:"+currentState+"lead to state:"+stateAfterAction);
						double maxVal=qVal[stateAfterAction][0];
						if(stateAfterAction==3){
							maxVal=1;
						}
						else if(stateAfterAction==7){
							maxVal=-1;
						}
						else{
						for(int j=1;j<4;j++){
							if(maxVal<qVal[stateAfterAction][j]){
								maxVal=qVal[stateAfterAction][j];
								maximumValuedActionInNextState=j;
							}
						}
						}
						frequency[currentState][currentAction]=frequency[currentState][currentAction]+1;
						qVal[currentState][currentAction]=qVal[currentState][currentAction]+
								(bonus*
								(-0.04+((discount*maxVal)-qVal[currentState][currentAction])));
						
						currentState=stateAfterAction;
						if(stateAfterAction==3||stateAfterAction==7){
							break;
						}
						//Exploration  function
						//Making 2 arrays for states which are in R+ category and for ones who aren't
						int maxActionForNextState=0;
						double maxQValue=0;int maxQIndex=0;
						for(int newAction=0;newAction<4;newAction++){
							if(frequency[currentState][newAction]==0){
								frequency[currentState][newAction]=1;								
							}
						}
						maxQValue=qVal[currentState][0]+(1/frequency[currentState][0]);
						for(int newAction=1;newAction<4;newAction++){
							if(maxQValue<(qVal[currentState][newAction]+(1/frequency[currentState][newAction]))){
								maxQIndex=newAction;
							}
						}
						maxActionForNextState=maxQIndex;
						//Pick the best action either randomized or from the ones where r>R_e
							System.out.println("Current state: "+currentState);
							currentAction=maxActionForNextState;
							System.out.println("Current action:"+currentAction);
						
					}//end while for one state s->final state
					System.out.println("Reached a final state from: "+i);
				}//end for i
				
				//Assigning value to the policy matrix
				double maxElementForPolicy=0;
				int maxActionForOptimalPolicy=0;
				for(int i=0;i<12;i++){
					if(i==3 ||i==7 || i==5){
						continue;
					}
					maxElementForPolicy=qVal[i][0];
					for(int j=1;j<4;j++){
						if(maxElementForPolicy<qVal[i][j]){
							maxElementForPolicy=qVal[i][j];
							maxActionForOptimalPolicy=j;
						}
					}
					policyForMatrix[i/4][i%4]=maxActionForOptimalPolicy;
				}
				//checking if the policy b/w successive iteration does not change
				for(int i=0;i<3;i++){
					for(int j=0;j<4;j++){
						if(policyForMatrix[i][j]!=oldPolicyMatrix[i][j]){
							continueFlag=false;
						}
					}
				}
				//assigning policyForMatrix to oldPolicyMatrix
				for(int i=0;i<3;i++){
					for(int j=0;j<4;j++){
						oldPolicyMatrix[i][j]=policyForMatrix[i][j];
						System.out.println(oldPolicyMatrix[i][j]+" ");
					}
					System.out.println();
				}
			}while(!continueFlag);
			System.out.println("Total no of iterations"+iterations);
			for(int i=0;i<3;i++){
				for(int j=0;j<4;j++){
					int k=(i*4)+j;
					if(k==3 || k==7){
						System.out.print("   Final   ");
					}
					else if(k==5){
						System.out.print("   Wall  ");
					}
					else if(policyForMatrix[i][j]==0){
						System.out.print("    Up   ");
					}
					else if(policyForMatrix[i][j]==1){
						System.out.print("   Down  ");
					}
					else if(policyForMatrix[i][j]==2){
						System.out.print("   Left   ");
					}
					if(policyForMatrix[i][j]==3){
						System.out.print("   Right   ");
					}

				}
				System.out.println();
			}
		}//end of main
		private static int findNextState(int currentState, int currentAction) {
			// TODO Auto-generated method stub
			//0 for up
			//1 for down
			//2 for left
			//3 for right
			int stateX=currentState/4;
			int stateY=currentState%4;
			if(currentAction==0){
				
				if(stateX==0 ||(stateX==2 && stateY==1)){
					return ((stateX*4)+stateY);
				}
				else{
					return (((stateX-1)*4)+stateY);
				}
			}
			if(currentAction==1){
				if(stateX==2 ||(stateX==0 && stateY==1)){
					return ((stateX*4)+stateY);
				}
				else{
					return (((stateX+1)*4)+stateY);
				}
			}
			if(currentAction==2){
				if(stateY==0 || (stateX==1 && stateY==2)){
					return ((stateX*4)+stateY);
				}
				else{
					return ((stateX*4)+stateY-1);
				}
			}
			if(currentAction==3){
				if(stateY==3  || (stateX==1 && stateY==0)){
					return ((stateX*4)+stateY);
				}
				else{
					return ((stateX*4)+stateY+1);
				}
			}
			//conditions for wall
			return ((stateX*4)+stateY);
		}

	}//end of class

