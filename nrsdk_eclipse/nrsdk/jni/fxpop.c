/*
   Fixed-point arithmetic

   Wenhui Jia	6/26/2000
*/

#include <stdio.h>
#include <stdlib.h>
#include "fxpop.h"

/* Local function */
Word16 sature(Word32 L_var1);


/************************************************************************                           
 * Function Name : sature                                       
 *                                                  
 * Purpose : Limit the 32 bit input to the range of a 16 bit word. 
 *                                                        
 * Input :                                             
 *                                                       
 *   L_var1                                             
 *           32 bit long signed integer (Word32) whose value falls in the 
 *            range : 0x8000 0000 <= L_var1 <= 0x7fff ffff.              
 *                                                                       
 * Output :   
 *                                                                      
 *   none                                                        
 *                                                             
 * Return Value :                                     
 *                                                      
 *  var_out                                         
 *            16 bit short signed integer (Word16) whose value falls in the 
 *            range : 0xffff 8000 <= var_out <= 0x0000 7fff.                
 **************************************************************************/
Word16 sature(Word32 L_var1)
{
    Word16 var_out;

    if (L_var1 > 0x00007fffL) {
        var_out = MAX_WORD16;
    }
    else if (L_var1 < (Word32)0xffff8000L) {
        var_out = MIN_WORD16;
    }
    else {
        var_out = (Word16) L_var1;
    }

    return(var_out);
}


/*___________________________________________________________________________
 |                                                                           |
 |   Function Name : add                                                     |
 |                                                                           |
 |   Purpose :                                                               |
 |                                                                           |
 |    Performs the addition (var1+var2) with overflow control and saturation;|
 |    the 16 bit result is set at +32767 when overflow occurs or at -32768   |
 |    when underflow occurs.                                                 |
 |                                                                           |
 |   Complexity weight : 1                                                   |
 |                                                                           |
 |   Inputs :                                                                |
 |                                                                           |
 |    var1                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var1 <= 0x0000 7fff.                   |
 |                                                                           |
 |    var2                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var1 <= 0x0000 7fff.                   |
 |                                                                           |
 |   Outputs :                                                               |
 |                                                                           |
 |    none                                                                   |
 |                                                                           |
 |   Return Value :                                                          |
 |                                                                           |
 |    var_out                                                                |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var_out <= 0x0000 7fff.                |
 |___________________________________________________________________________|
*/

Word16 add(Word16 var1,Word16 var2)
{
    Word16 var_out;
    Word32 L_sum;

    L_sum = (Word32) var1 + (Word32) var2;
    var_out = sature(L_sum);
    return(var_out);
}

/*___________________________________________________________________________
 |                                                                           |
 |   Function Name : sub                                                     |
 |                                                                           |
 |   Purpose :                                                               |
 |                                                                           |
 |    Performs the subtraction (var1+var2) with overflow control and satu-   |
 |    ration; the 16 bit result is set at +32767 when overflow occurs or at  |
 |    -32768 when underflow occurs.                                          |
 |                                                                           |
 |   Complexity weight : 1                                                   |
 |                                                                           |
 |   Inputs :                                                                |
 |                                                                           |
 |    var1                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var1 <= 0x0000 7fff.                   |
 |                                                                           |
 |    var2                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var1 <= 0x0000 7fff.                   |
 |                                                                           |
 |   Outputs :                                                               |
 |                                                                           |
 |    none                                                                   |
 |                                                                           |
 |   Return Value :                                                          |
 |                                                                           |
 |    var_out                                                                |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var_out <= 0x0000 7fff.                |
 |___________________________________________________________________________|
*/

Word16 sub(Word16 var1,Word16 var2)
{
    Word16 var_out;
    Word32 L_diff;

    L_diff = (Word32) var1 -  (Word32) var2;
    var_out = sature(L_diff);
    return(var_out);
}

/*___________________________________________________________________________
 |                                                                           |
 |   Function Name : abs_s                                                   |
 |                                                                           |
 |   Purpose :                                                               |
 |                                                                           |
 |    Absolute value of var1; abs_s(-32768) = 32767.                         |
 |                                                                           |
 |   Complexity weight : 1                                                   |
 |                                                                           |
 |   Inputs :                                                                |
 |                                                                           |
 |    var1                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var1 <= 0x0000 7fff.                   |
 |                                                                           |
 |   Outputs :                                                               |
 |                                                                           |
 |    none                                                                   |
 |                                                                           |
 |   Return Value :                                                          |
 |                                                                           |
 |    var_out                                                                |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0x0000 0000 <= var_out <= 0x0000 7fff.                |
 |___________________________________________________________________________|
*/

Word16 abs_s(Word16 var1)
{
    Word16 var_out;

    if (var1 == (Word16)0x8000 ) {
        var_out = MAX_WORD16;
    }
    else {
        if (var1 < 0) {
            var_out = -var1;
        }
        else {
            var_out = var1;
        }
    }

    return(var_out);
}


/*___________________________________________________________________________
 |                                                                           |
 |   Function Name : mult                                                    |
 |                                                                           |
 |   Purpose :                                                               |
 |                                                                           |
 |    Performs the multiplication of var1 by var2 and gives a 16 bit result  |
 |    which is scaled i.e.:                                                  |
 |             mult(var1,var2) = (var1 times var2)>>15 and               |
 |             mult(-32768,-32768) = 32767.                                  |
 |                                                                           |
 |   Complexity weight : 1                                                   |
 |                                                                           |
 |   Inputs :                                                                |
 |                                                                           |
 |    var1                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var1 <= 0x0000 7fff.                   |
 |                                                                           |
 |    var2                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var1 <= 0x0000 7fff.                   |
 |                                                                           |
 |   Outputs :                                                               |
 |                                                                           |
 |    none                                                                   |
 |                                                                           |
 |   Return Value :                                                          |
 |                                                                           |
 |    var_out                                                                |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var_out <= 0x0000 7fff.                |
 |___________________________________________________________________________|
*/

Word16 mult(Word16 var1, Word16 var2)
{
    Word16 var_out;
    Word32 L_product;

    if (var1 == MIN_WORD16 && var2 == MIN_WORD16) {
       var_out = MAX_WORD16;
	}
	else {
       L_product = (Word32)var1 * (Word32)var2;
	   var_out = (Word16) (L_product >> 15);
	}

    return(var_out);
}


/*___________________________________________________________________________
 |                                                                           |
 |   Function Name : mult_r                                                  |
 |                                                                           |
 |   Purpose :                                                               |
 |                                                                           |
 |     Same as MULT but with rounding, i.e.:                                 |
 |                                                                           |
 |         mult(var1,var2) = ((var1 times var2)+16384)>>15 and               |
 |         mult(-32768,-32768) = 32767.                                      |
 |                                                                           |
 |   Complexity weight : 1                                                   |
 |                                                                           |
 |   Inputs :                                                                |
 |                                                                           |
 |    var1                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var1 <= 0x0000 7fff.                   |
 |                                                                           |
 |    var2                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var1 <= 0x0000 7fff.                   |
 |                                                                           |
 |   Outputs :                                                               |
 |                                                                           |
 |    none                                                                   |
 |                                                                           |
 |   Return Value :                                                          |
 |                                                                           |
 |    var_out                                                                |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var_out <= 0x0000 7fff.                |
 |___________________________________________________________________________|
*/

Word16 mult_r(Word16 var1, Word16 var2)
{
    Word16 var_out;
    Word32 L_product;

    if (var1 == MIN_WORD16 && var2 == MIN_WORD16) {
       var_out = MAX_WORD16;
	}
	else {
       L_product = (Word32)var1 * (Word32)var2;
	   L_product += 16384L;
	   var_out = (Word16) (L_product >> 15);
	}

    return(var_out);
}

/*___________________________________________________________________________
 |                                                                           |
 |   Function Name : div16                                                     |
 |                                                                           |
 |   Purpose :                                                               |
 |                                                                           |
 |   Produces a result which is the fractional integer division of var1 by   |
 |   var2; var1 and var2 must be positive and var2 must be greater or equal  |
 |   to var1; the result is positive (leading bit equal to 0) and truncated  |
 |   to 16 bits.                                                             |
 |   If var1 = var2 then div(var1,var2) = 32767.                             |
 |                                                                           |
 |   Complexity weight : 18                                                  |
 |                                                                           |
 |   Inputs :                                                                |
 |                                                                           |
 |    var1                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0x0000 0000 <= var1 <= var2 and var2 != 0.            |
 |                                                                           |
 |    var2                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : var1 <= var2 <= 0x0000 7fff and var2 != 0.            |
 |                                                                           |
 |   Outputs :                                                               |
 |                                                                           |
 |    none                                                                   |
 |                                                                           |
 |   Return Value :                                                          |
 |                                                                           |
 |    var_out                                                                |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0x0000 0000 <= var_out <= 0x0000 7fff.                |
 |             It's a Q15 value (point between b15 and b14).                 |
 |___________________________________________________________________________|
*/

Word16 div16(Word16 var1, Word16 var2)
{
    Word16 var_out = 0;
    Word16 iteration;
    Word32 L_num;
    Word32 L_denum;

    if ((var1 > var2) || (var1 < 0) || (var2 < 0)) {
        printf("Division Error\n");
        exit(0);
    }

    if (var2 == 0) {
        printf("Division by 0, Fatal error \n");
        exit(0);
    }

    if (var1 == 0) {
        var_out = 0;
    }
    else {
        if (var1 == var2) {
            var_out = MAX_WORD16;
        }
        else {
            L_num = (Word32) var1;
            L_denum = (Word32) var2;

            for(iteration=0; iteration<15; iteration++) {
                var_out <<=1;
                L_num <<= 1;
                if (L_num >= L_denum) {
                    L_num = L_sub(L_num,L_denum);
                    var_out = add(var_out, 1);
                }
            }
        }
    }

    return(var_out);
}


/*___________________________________________________________________________
 |                                                                           |
 |   Function Name : L_mult                                                  |
 |                                                                           |
 |   Purpose :                                                               |
 |                                                                           |
 |   L_mult is the 32 bit result of the multiplication of var1 times var2    |
 |   with one shift left i.e.:                                               |
 |        L_mult(var1,var2) = shl((var1 times var2),1) and                   |
 |        L_mult(-32768,-32768) = 2147483647.                                |
 |                                                                           |
 |   Complexity weight : 1                                                   |
 |                                                                           |
 |   Inputs :                                                                |
 |                                                                           |
 |    var1                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var1 <= 0x0000 7fff.                   |
 |                                                                           |
 |    var2                                                                   |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0xffff 8000 <= var1 <= 0x0000 7fff.                   |
 |                                                                           |
 |   Outputs :                                                               |
 |                                                                           |
 |    none                                                                   |
 |                                                                           |
 |   Return Value :                                                          |
 |                                                                           |
 |    L_var_out                                                              |
 |             32 bit long signed integer (Word32) whose value falls in the  |
 |             range : 0x8000 0000 <= L_var_out <= 0x7fff ffff.              |
 |___________________________________________________________________________|
*/

Word32 L_mult(Word16 var1,Word16 var2)
{
    Word32 L_var_out;

    L_var_out = (Word32)var1 * (Word32)var2;
    L_var_out <<= 1;

    return(L_var_out);
}

/*___________________________________________________________________________
 |                                                                           |
 |   Function Name : L_add                                                   |
 |                                                                           |
 |   Purpose :                                                               |
 |                                                                           |
 |   32 bits addition of the two 32 bits variables (L_var1+L_var2) with      |
 |   overflow control and saturation; the result is set at +214783647 when   |
 |   overflow occurs or at -214783648 when underflow occurs.                 |
 |                                                                           |
 |   Complexity weight : 2                                                   |
 |                                                                           |
 |   Inputs :                                                                |
 |                                                                           |
 |    L_var1   32 bit long signed integer (Word32) whose value falls in the  |
 |             range : 0x8000 0000 <= L_var3 <= 0x7fff ffff.                 |
 |                                                                           |
 |    L_var2   32 bit long signed integer (Word32) whose value falls in the  |
 |             range : 0x8000 0000 <= L_var3 <= 0x7fff ffff.                 |
 |                                                                           |
 |   Outputs :                                                               |
 |                                                                           |
 |    none                                                                   |
 |                                                                           |
 |   Return Value :                                                          |
 |                                                                           |
 |    L_var_out                                                              |
 |             32 bit long signed integer (Word32) whose value falls in the  |
 |             range : 0x8000 0000 <= L_var_out <= 0x7fff ffff.              |
 |___________________________________________________________________________|
*/

Word32 L_add(Word32 L_var1, Word32 L_var2)
{
    Word32 L_var_out;

    L_var_out = L_var1 + L_var2;

    if (((L_var1 ^ L_var2) & MIN_WORD32) == 0L) {
        if ((L_var_out ^ L_var1) & MIN_WORD32) {
            L_var_out = (L_var1 < 0L) ? MIN_WORD32 : MAX_WORD32;
        }
    }
    return(L_var_out);
}

/*___________________________________________________________________________
 |                                                                           |
 |   Function Name : L_sub                                                   |
 |                                                                           |
 |   Purpose :                                                               |
 |                                                                           |
 |   32 bits subtraction of the two 32 bits variables (L_var1-L_var2) with   |
 |   overflow control and saturation; the result is set at +214783647 when   |
 |   overflow occurs or at -214783648 when underflow occurs.                 |
 |                                                                           |
 |   Complexity weight : 2                                                   |
 |                                                                           |
 |   Inputs :                                                                |
 |                                                                           |
 |    L_var1   32 bit long signed integer (Word32) whose value falls in the  |
 |             range : 0x8000 0000 <= L_var3 <= 0x7fff ffff.                 |
 |                                                                           |
 |    L_var2   32 bit long signed integer (Word32) whose value falls in the  |
 |             range : 0x8000 0000 <= L_var3 <= 0x7fff ffff.                 |
 |                                                                           |
 |   Outputs :                                                               |
 |                                                                           |
 |    none                                                                   |
 |                                                                           |
 |   Return Value :                                                          |
 |                                                                           |
 |    L_var_out                                                              |
 |             32 bit long signed integer (Word32) whose value falls in the  |
 |             range : 0x8000 0000 <= L_var_out <= 0x7fff ffff.              |
 |___________________________________________________________________________|
*/

Word32 L_sub(Word32 L_var1, Word32 L_var2)
{
    Word32 L_var_out;

    L_var_out = L_var1 - L_var2;

    if (((L_var1 ^ L_var2) & MIN_WORD32) != 0L) {
        if ((L_var_out ^ L_var1) & MIN_WORD32) {
            L_var_out = (L_var1 < 0L) ? MIN_WORD32 : MAX_WORD32;
        }
    }
    return(L_var_out);
}



/*___________________________________________________________________________
 |                                                                           |
 |   Function Name : norm                                                  |
 |                                                                           |
 |   Purpose :                                                               |
 |                                                                           |
 |   Produces the number of left shift needed to normalize the 32 bit varia- |
 |   ble l_var1 for positive values on the interval with minimum of          |
 |   1073741824 and maximum of 2147483647, and for negative values on the in-|
 |   terval with minimum of -2147483648 and maximum of -1073741824; in order |
 |   to normalize the result, the following operation must be done :         |
 |                   norm_L_var1 = L_shl(L_var1,norm_l(L_var1)).             |
 |                                                                           |
 |   Complexity weight : 30                                                  |
 |                                                                           |
 |   Inputs :                                                                |
 |                                                                           |
 |    L_var1                                                                 |
 |             32 bit long signed integer (Word32) whose value falls in the  |
 |             range : 0x8000 0000 <= var1 <= 0x7fff ffff.                   |
 |                                                                           |
 |   Outputs :                                                               |
 |                                                                           |
 |    none                                                                   |
 |                                                                           |
 |   Return Value :                                                          |
 |                                                                           |
 |    var_out                                                                |
 |             16 bit short signed integer (Word16) whose value falls in the |
 |             range : 0x0000 0000 <= var_out <= 0x0000 001f.                |
 |___________________________________________________________________________|
*/

Word16 norm_l(Word32 L_var1)
{
    Word16 var_out;

    if (L_var1 == 0L) {
        var_out = (Word16)0;
    }
    else {
        if (L_var1 == (Word32)0xffffffffL) {
            var_out = (Word16)31;
        }
        else {
            if (L_var1 < 0L) {
                L_var1 = ~L_var1;
            }

            for(var_out = (Word16)0;L_var1 < (Word32)0x40000000L;var_out++) {
                L_var1 <<= 1L;
            }
        }
    }

    return(var_out);
}


Word32 L_abs(Word32 L_var1)
{

	Word32 L_var_out;

	if (L_var1 == MIN_WORD32) {
		L_var_out = MAX_WORD32;
	}
	else {
		if (L_var1 < 0L) {
			L_var_out = -L_var1;
		}
		else {
			L_var_out = L_var1;
		}
	}
	return(L_var_out);
}