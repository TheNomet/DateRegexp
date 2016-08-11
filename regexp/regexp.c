#include <stdio.h>
#include <string.h>
#include <stdlib.h>


void min10(int from, int to, int zeronum, char *partialsolution);
void min100(int from[2],int to0[2],int zeronum, char *partialsolution);
void min1000(int from [3], int to0[3], int zeronum);
void min10000(int from[4], int to0[4], int zeronum);

int N; //used as unique identifier, to distinguish between different runs


int main(int argc, char **argv){

    int from,to;
    int *to0;
    int *from0;
    char solution[1024];


    if(argc!=4){
        printf("syntax is: '<prog> <from> <to> <id>'\n");
        return -1;
    }
    N=atoi(argv[3]);
    from=atoi(argv[1]);
    to=atoi(argv[2]);
    if (from>to){
        printf("error: 'from -%d-' freater than 'to -%d-'\n",from,to);
        return 1;
    }
    if(to<10){
        //printf("to %d from %d\n",to,from);
        min10(from,to,0,NULL);
    }
    else if(to<100){
        to0=(int*)malloc(sizeof(int)*2);
        from0=(int*)malloc(sizeof(int)*2);
        to0[0]=argv[2][0]-'0';
        to0[1]=argv[2][1]-'0';
        if(from<10){
            min10(from,9,0,NULL);
            from0[0]=0;
            from0[1]=0;
            min100(from0,to0,-2,NULL);//-1
        }
        else{
            from0[0]=argv[1][0]-'0';
            from0[1]=argv[1][1]-'0';
            min100(from0,to0,-1,NULL);
        }

        free(to0);
        free(from0);
    }
    else if(to<1000){
        to0=(int*)malloc(sizeof(int)*3);
        from0=(int*)malloc(sizeof(int)*2);
        to0[0]=argv[2][0]-'0';
        to0[1]=argv[2][1]-'0';
        to0[2]=argv[2][2]-'0';
        if(from<10){
            from0[1]=0;
            from0[0]=0;
            from0[2]=from;
        }
        else if(from<100){
            from0[1]=argv[1][0]-'0';
            from0[2]=argv[1][1]-'0';
            from0[0]=0;
        }
        else {
            from0[0]=argv[1][0]-'0';
            from0[1]=argv[1][1]-'0';
            from0[2]=argv[1][2]-'0';
        }
        min1000(from0,to0,-1);
        free(from0);
        free(to0);
    }
    else if(to<10000){
        to0=(int*)malloc(sizeof(int)*4);
        from0=(int*)malloc(sizeof(int)*2);
        to0[0]=argv[2][0]-'0';
        to0[1]=argv[2][1]-'0';
        to0[2]=argv[2][2]-'0';
        to0[3]=argv[2][3]-'0';
        if(from<10){
            from0[1]=0;
            from0[0]=0;
            from0[2]=0;
            from0[3]=from;
        }
        else if(from<100){
            from0[1]=0;
            from0[2]=argv[1][0]-'0';
            from0[3]=argv[1][1]-'0';
            from0[0]=0;
        }
        else if(from<1000) {
            from0[1]=argv[1][0]-'0';
            from0[2]=argv[1][1]-'0';
            from0[3]=argv[1][2]-'0';
            from0[0]=0;
        }
        else{
            from0[0]=argv[1][0]-'0';
            from0[1]=argv[1][1]-'0';
            from0[2]=argv[1][2]-'0';
            from0[3]=argv[1][3]-'0';
        }
        min10000(from0,to0,0);
        free(to0);
        free(from0);
    }

}
/*
	Generates the regexp for numbers < 10

	@param from
		Lower bound number
	@param to
		Upper bound number
	@param zeronum
		Number of zeros to put before the regexp
	@param partialsolution
		If NULL -> regexp written on stdout
		else regexp returned in the string
*/
void min10(int from, int to, int zeronum, char *partialsolution){
    char solution[1024];
    int i;
    if(from==to)
        sprintf(solution,"%d",from);
    else if(to>0)
        sprintf(solution,"$%d-%d$",from,to);
    else sprintf(solution,"%d",0);
    if(partialsolution==NULL){
        printf("n%d = ",N);
        for(i=0;i<zeronum;i++)
            printf("0");
        if(zeronum!=0) printf("(");
        printf("%s",solution);
        if(zeronum!=0) printf(")");
        printf(";\n");
    }
    else{
        for(i=0;i<zeronum;i++)
            strcat(partialsolution,"0");
        strcat(partialsolution,solution);
    }
}

/*
	Generates the regexp for numbers < 100

	@param from
		Lower bound number, composed by two numbers
	@param to
		Upper bound number, composed by two numbers
	@param zeronum
		Number of zeros to put before the regexp
		if zeronum==-2 -> in the result is also included the id of the regexp returned by min10
	@param partialsolution
		If NULL -> regexp written on stdout
		else regexp returned in the string
*/
void min100(int from[2], int to0[2],int zeronum, char *partialsolution){
    char solution[1024]="";//final solution
    char partial[1024]="";//second partial solution
    char partialf[1024]="";//partial solution for "from"
    int i;
    int flag=1;
    if(from[0]>0 && from[0]<to0[0]){//from >= 10 && from < to0 ---> fill up partialf
        if(from[1]==0)
            from[0]=from[0]-1;
        else
        if(from[1]<9){
            sprintf(partialf,"%d$%d-9$",from[0],from[1]);
        }
        else
            sprintf(partialf,"%d9",from[0]);
    }
    if (from[0]>0 && from[0]==to0[0]){ //from >= 10 && dozens of from == dozens of to0
        if(from[1]!=to0[1])
            sprintf(partialf,"%d$%d-%d$",from[0],from[1],to0[1]);
        else
            sprintf(partialf,"%d%d",from[0],from[1]);
        flag=0;
    }
    else if(to0[1]!=9){//from >= 10 && from < to0 --> fill up partial
        if(to0[0]>from[0]+1){
            if(to0[0]-1>from[0]+1){
                sprintf(partial,"[%d-%d]$0-9$|%d",from[0]+1,to0[0]-1,to0[0]);
            }
            else sprintf(partial,"%d$0-9$|%d",from[0]+1,to0[0]);
        }
        else sprintf(partial,"%d",from[0]+1);
		//fill up solution
        if(to0[1]>0){
            sprintf(solution,"%s$0-%d$",partial,to0[1]);
        }
        else sprintf(solution,"%s0",partial);
    }
    else{
        if(from[0]+1!=9 && from[0]+1<to0[0])
            sprintf(solution,"[%d-%d]$0-9$",from[0]+1,to0[0]);
        else
            sprintf(solution,"%d$0-9$",to0[0]);
    }
	//print on screen
    if(partialsolution==NULL){
        printf("nX%d =  ",N);
        for(i=0;i<zeronum;i++)
            printf("0");

        if(zeronum>0)
            printf("(");
        if(zeronum==-2){ //concatenate also the solution of min10
            printf("{n%d}|",N);
        }
        printf("%s",partialf);
        if(strcmp(partialf,"")!=0 && strcmp(solution,"")!=0)
            printf("|");
        printf("%s",solution);
        if(zeronum>0)
            printf(")");
        printf(";\n");
    }
	//fill partialsolution
    else{
       // if(flag)
       //     strcat(partialsolution,"(");
        for(i=0;i<zeronum;i++)
            strcat(partialsolution,"0");

        if(zeronum>0)
            strcat(partialsolution,"(");

        strcat(partialsolution,partialf);

        if(strcmp(partialf,"")!=0 && strcmp(solution,"")!=0)
            strcat(partialsolution,"|");
        strcat(partialsolution,solution);

        if(zeronum>0)
            strcat(partialsolution,")");
       // if(flag)
        //    strcat(partialsolution,")");
    }
}

/*
	Generates the regexp for numbers < 100

	@param from
		Lower bound number, composed by three numbers
	@param to
		Upper bound number, composed by three numbers
	@param zeronum
		Number of zeros to put before the regexp
		if zeronum==-2 -> in the result is also included the id of the regexp returned by min10
*/

void min1000(int from[3], int to0[3], int zeronum){
    char solution[1024]="";
    char partialf[1024]="";
    char pp[1024]="";
    char pp1[1024]="";
    int i;
    char n;
    int flag=0;
    int multiple100=0;

    if(zeronum==0) n='s';
    else n='n';

    if(from[0]==0 && from[1]==0){//from < 10
    N=N+10;
    min10(from[2],9,zeronum+1,NULL);
    sprintf(pp,"{n%d}",N);
    N=N-10;
    }

    if(from[0]==0){ //from < 100 : if also < 10, also min10 was called in the if above
        N=N+10;
        int dup[2]={9,9};
        min100(from+1,dup,zeronum,NULL);
        sprintf(pp1,"{nX%d}",N);
        N=N-10;
        from[1]=from[2]=0;
    }
    char partial[1024];

    if(from[0]>0){ // from > 100
        char lower[1024]="";
        char lower1[1024]="";
        int duo[2]={9,9};
        if(from[1]>0){//greater than 10
            if(to0[0]>from[0]){
                min100(from+1,duo,-2,lower);
			}
            else{
                min100(from+1,to0+1,-2,lower);
                flag=1;
            }
        }
        if(from[1]==0){//min than 10
            if(to0[0]>from[0]){//to 9 and then from 10 to 99
                if(from[2]==0){
                    from[0]--;
                    multiple100=1;
                }
                else{
                min10(from[2],9,1,lower1); // the one is used to put a 0 in front of [0-9] -> 0[0-9]
                sprintf(lower,"|");
                from[0]=1;
                from[1]=0;
                min100(from,duo,-2,lower);
                }
            }
            else {//ex 124 166 ---> no need to call the second part
                flag=1;
                if(to0[1]>0 && from[1]==0){//to 9 and from 10 to to0
                    min10(from[2],9,1,lower1);
                    sprintf(lower,"|");
                    from[0]=1;
                    from[1]=0;
                    min100(from,to0+1,-2,lower);
                }
                else if(to0[1]>0 && from[1]>0)
                    min100(from+1,to0+1,-2,lower);
                else if(to0[1]==0 && from[1]==0){
                    min10(from[2],to0[2],1,lower1);
                }
            }
        }
		//printf("loew1 %s\n",lower1);
		//printf("loew %s\n",lower);
		if(!multiple100){
            sprintf(partialf,"%d(%s%s)",from[0],lower1,lower);
            from[1]=from[2]=0;
        }
    }

	//second part
	if(!flag){
        if(to0[0]==9 && to0[1]==9 && to0[2]==9)
            sprintf(solution,"[%d-%d][0-9]$0-9$",from[0]+1,to0[0]);
        else{
            if(to0[0]>from[0]+1){
                if(to0[0]-1>from[0]+1)
                    sprintf(partial,"[%d-%d][0-9]$0-9$|%d",from[0]+1,to0[0]-1,to0[0]);
                else sprintf(partial,"%d[0-9]$0-9$|%d",from[0]+1,to0[0]);
            }
            else sprintf(partial,"%d",from[0]+1);
            if(to0[1]>0) {
                min100(from+1,to0+1,0,NULL);
                if(from[1]==0)
                    sprintf(solution,"%s(0$%d-9$|{nX%d})",partial,from[2],N);
                else sprintf(solution,"%s{nX%d}",partial,N);
            }
            else if(to0[2]>0){
                min10(from[2],to0[2],1,NULL);
                sprintf(solution,"%s{n%d}",partial,N);
            }
            else sprintf(solution,"%s00",partial);
        }
    }
    flag=0;
    printf("nXX%d = %s",N,pp);
    if(strcmp(pp,"")!=0) flag=1;
    if(strcmp(pp1,"")!=0)
        if(flag)
            printf("|");
        else flag=1;
    printf("%s",pp1);
    if(strcmp(partialf,"")!=0)
        if(flag)
            printf("|");
        else flag=1;
    printf("%s",partialf);
    if(strcmp(solution,"")!=0)
    if(flag)
        printf("|");
    else flag=1;
    printf("%s;\n",solution);

}

void min10000(int from[4], int to0[4], int zeronum){
    char solution[1024];
    char n;
    int i;
    char pp[1024],pp1[1024],pp2[1024],pp3[1024];
	char partial[1024],dec[1024];
    int flag = 0;

    if(zeronum==0) n='s';
    else n='n';

    int trio[3]={9,9,9};
    if(from[0]==0){//from<1000
        N=N+10;
        sprintf(pp3,"{nXX%d}",N);
        min1000(from+1,trio,-1);
        N=N-10;
        from[0]=1;
        from[1]=from[2]=from[3]=0;
        flag=1;
    }

    if(to0[0]>from[0]){
        if(to0[0]-1>=from[0]+1){

            if(from[1]==0 && from[3]==0 && from[2]==0)
                sprintf(partial,"[%d-%d][0-9]{2}$0-9$|%d",from[0],to0[0]-1,to0[0]);
            else{
                N=N+11;
               // if(from[1]>0){
                    if(to0[0]-1>from[0]+1)
                        sprintf(partial,"%d({nXX%d})|[%d-%d][0-9]{2}$0-9$|%d",from[0],N,from[0]+1,to0[0]-1,to0[0]);
                    else
                        sprintf(partial,"%d({nXX%d})|%d[0-9]{2}$0-9$|%d",from[0],N,from[0]+1,to0[0]);
                    min1000(from+1,trio,1);
                //}
                N=N-11;

            }

        }

        else{
            if(from[1]==0 && from[3]==0 && from[2]==0)
                sprintf(partial,"%d([0-9]){2}$0-9$|%d",from[0],to0[0]);
            else{
                N=N+11;
                sprintf(partial,"%d({nXX%d})|%d",from[0],N,to0[0]);
                min1000(from+1,trio,1);
                N=N-11;
            }
        }

    }
    else sprintf(partial,"%d",from[0]);
    if(to0[1]>0) {
        min1000(from+1,to0+1,1);
        sprintf(solution,"%s({nXX%d})",partial,N);
    }
    else if(to0[2]>0){
        min100(from+2,to0+2,1,NULL);
        if(from[2]==0 && from[1]>0){
            min10(0,9,2,NULL);
            sprintf(solution,"%s({n%d}|{nX%d})",partial,N,N);
        }
        else
            sprintf(solution,"%s({nX%d})",partial,N);
    }
    else if(to0[3]>0){
        min10(from[3],to0[3],2,NULL);
        sprintf(solution,"%s{n%d}",partial,N);
    }
    else sprintf(solution,"%s000",partial);
    if(flag)
        printf("nXXX%d = %s|%s;\n",N,pp3,solution);
    else
        printf("nXXX%d = %s;\n",N,solution);
}
