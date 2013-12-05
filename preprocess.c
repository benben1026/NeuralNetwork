#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int arg, char ** args){
	int start_pos, length, data, i;
	char * aline;
	FILE * raw_data, * dataset, * answer;
	raw_data = fopen(args[1], "r");
	dataset = fopen(args[2], "w");
	answer = fopen(args[3], "w");

	start_pos = 1;
	length = 30;

	aline = malloc(sizeof(char) * 200);
	while(1){
		if(fscanf(raw_data, "%s", aline) == -1){
			break;
		}
		data = 0; 
		for(i = 0; aline[i] != 0; i++){
			if(data < length && i >= (start_pos - 1) &&  aline[i] != ','){
				fprintf(dataset, "%c ", aline[i]);
				data ++;
			}
			if(i == (strlen(aline) - 1)){
				fprintf(answer, "%c\n", aline[i]);
			}
		}
		fprintf(dataset, "\n");
	}
	fclose(raw_data);
	fclose(dataset);
	fclose(answer);
	return 0;
}