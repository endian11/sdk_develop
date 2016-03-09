/**********************************************************************
 * FILE:     ec_lms.c
 * CREATED:  5/10/2001
 * AUTHOR:   Wenhui Jia
 * PURPOSE:  Normalized LMS echo canceller
 *
 * Modified to any frame size, 5/24/2001, Wenhui Jia                                      
 ***********************************************************************/

#include "ec_lms.h"
#include "fxpop.h"

long subc(long acc, long sub);

void init_ec(struct ec_lms_struct *ec)
{
	short  i;

	ec->TxMax_128 = 0;		/* was 1024 */
	ec->TxMag = 0;			/* was 1024 */
	ec->RxMag1 = 0;
	ec->RxMax1 = 0;
	ec->RxMag2 = 0;
	ec->RxMax2 = 0;
	ec->HangCount = 1;
	ec->Timer = 0;
	ec->Shift = -1;

	for (i=0;i<LMSSIZE;i++) {
		ec->Coeff[i] = 0;
		ec->stat[i] = 0;
	}
}

void ec_lms(struct ec_lms_struct *ec, short *BufTx, short *BufRx, short N)
{
	long   L_Acc, L_AccB, L_temp, L_sum1, L_sum2;
	short  TxMax, RxMag1a, RxMax1a, InvMag;
	short  clean, dc1, dc2, temp, FLAG_NoAdapt;
	short  i, j;
    short lookback;
    short halfsecond;


	/* Remove DC */
	/*
	L_sum1 = 0;
	L_sum2 = 0;
	for (i=0;i<N;i++) {
		L_sum1 += BufTx[i];
		L_sum2 += BufRx[i];
	}
	dc1 = L_sum1/N;
	dc2 = L_sum2/N;
	for (i=0;i<N;i++) {
		BufTx[i] -= dc1;
		BufRx[i] -= dc2;
	}
	*/

    /* extra samples required for double talk detection */
    lookback = LMSSIZE - N;

    /* assuming 8kHz sampling rate */
    halfsecond = (8000/2) / N;

	/* Calculate TxMax for the recent block of 128 samples */
	/* Using exponential window with alpha = 1/32 */
	/* corresponds to a time constant of 4 ms */
	L_AccB = (long)ec->TxMax_128 << 16;
	temp = ec->TxMax_128;
	for (i=0;i<N;i++) {			// temp = 31/32 temp + 1/32 |BufTx[i]|
		L_Acc = (long)BufTx[-lookback+i] << (16-3);
		L_Acc = L_abs(L_Acc);
		L_Acc = L_sub(L_Acc, (long)temp<<(16-3));
		L_Acc = L_add(L_Acc, (long)temp<<16);
		temp = L_Acc >> 16;
		if (L_Acc > L_AccB)
			L_AccB = L_Acc;
	}
	ec->TxMax_128 = temp;
	for (i=0;i<lookback;i++) {			// temp = 31/32 temp + 1/32 |BufTx[i]|
		L_Acc = (long)BufTx[-lookback+N+i] << (16-3);
		L_Acc = L_abs(L_Acc);
		L_Acc = L_sub(L_Acc, (long)temp<<(16-3));
		L_Acc = L_add(L_Acc, (long)temp<<16);
		temp = L_Acc >> 16;
		if (L_Acc > L_AccB)
			L_AccB = L_Acc;
	}
	TxMax = L_AccB >> 16;

	/* Upon energy onset, start the Timer */
	if (TxMax > 100 && ec->Shift==-1)
		ec->Shift = 0;

	/* Increment half-second Timer */
	/* will use a small adptation rate after 0.5 second */
	/* 0.5 second --> 4000 samples */
	/* for 5 ms, 40 samples/frame, the timeout is 100 */
	if (ec->Shift==0)
		ec->Timer ++;


	/* scaling Tx level by 80%*/
	/* TxMax = (TxMax * K0_8) >> 15; */
	TxMax = (TxMax * LMSATTEN6) >> 15; 


	/* Calculate RxMag1 & RxMax1 for the recent block of N samples */
	/* Using the same window as in calculating TxMax */
	RxMag1a = ec->RxMag1;
	RxMax1a = ec->RxMax1;
	L_AccB = 0;
	for (i=0;i<N;i++) {			// RxMag1 = 31/32 RxMag1 + 1/32 |BufRx[i]|
		L_Acc = (long)BufRx[i] << (16-3);
		L_Acc = L_abs(L_Acc);
		L_Acc = L_sub(L_Acc, (long)ec->RxMag1<<(16-3));
		L_Acc = L_add(L_Acc, (long)ec->RxMag1<<16);
		ec->RxMag1 = L_Acc >> 16;
		if (L_Acc > L_AccB)
			L_AccB = L_Acc;
	}
	ec->RxMax1 = L_AccB >> 16;

/*
printf("\nTxMax=%d  RxMax1=%d  RxMax2=%d  RxMax1a=%d",
	   TxMax, ec->RxMax1, ec->RxMax2, RxMax1a);
*/
	/* Double talk detection */
	/* 1) Rx level > 80% of Tx level */
	/* 2) Attenuation achieved in last frame is less than 10 dB */
	L_temp = (long)(ec->RxMax2+1) << 15;
	if ( (ec->RxMax1+1) > TxMax ) {
		ec->HangCount = LMSHANGSET;
	}
	else if ( /* ec->Shift==1 && */ LMSATTEN10*RxMax1a < L_temp ) {
		ec->HangCount = LMSHANGSET;
/* printf("Yes  "); */
	}
	else {
		ec->HangCount -= N;
		if (ec->HangCount < 0)
			ec->HangCount = 0;
	}

/*
printf("  %d  ", ec->Shift);
*/

	/* Decision of adaptation */
	if ( ec->Shift==0 || ec->HangCount==0)
		FLAG_NoAdapt = 0;		/* Cancel and adaptation */
	else
		FLAG_NoAdapt = 1;		/* Cancel but no adaptation */


	/* The transversal filter is maintained as
	 *
	 *		Index	0		 1		   ...	126		127
	 *		-----------------------------------------------
	 *		FIR		a[127]   a[126]    ...  a[1]    a[0]
	 *		State	x[n-127] x[n-126]  ...	x[n-1]  x[n-0]	
	**/
	for (i=0;i<N;i++) {
			/* echo prediction, sum{k=0...127} a[k]x[i-k] */
			L_Acc = 0;
			for (j=0;j<LMSSIZE;j++) {
				L_Acc += ec->Coeff[j] * BufTx[i-(LMSSIZE-1)+j];
			}
			/* remove echo, which is in Acc in Q15 format */
			L_Acc -= (long)BufRx[i] << 15;
			L_Acc = -L_Acc;
			L_Acc += 1<<14;		/* rounding */
			L_Acc >>= 15;		/* Q0 */
			clean = (short)L_Acc;
			BufRx[i] = clean;
			/* Calculate TxMag on a sample-by-sample basis */
			/* Using exponential window with alpha = 1/200 */
			/* corresponds to a time constant of 25 ms */
			temp = abs_s(BufTx[i]);
			temp += 130>>1;		/* to avoid division by 0 */
			L_Acc = L_add(temp*K0_005, ec->TxMag*K0_995);
			ec->TxMag = L_Acc >> 15;
			/* Normalization, InvMag = 1 / TxMag */
			L_Acc = (long)256 << 15;
			for (j=0;j<16;j++)
				L_Acc = subc(L_Acc, (long)ec->TxMag<<15);
			InvMag = ((L_Acc & 0x0000ffffL)+1) >> 1;
			/* Weighted error, Acc = clean * InvMag * InvMag */
			L_Acc = clean * InvMag;
			clean = L_Acc >> 15;
			L_Acc = clean * InvMag;
			/* step size controlled by SHIFT */
			if (ec->Shift <= 0)
				L_Acc >>= 8;
			else
				L_Acc >>= 10;
			/* Limiting to [-2000, 2000] */
			if (L_Acc > 2000)
				L_Acc = 2000;
			if (L_Acc < -2000)
				L_Acc = -2000;

			clean = L_Acc;
			/* If No Adaptation, set clean=0 */
			if (FLAG_NoAdapt==1)
				clean = 0;
			/* Adaptation, a[k] = a[k] + clean[i]*x[i-k], k=0...127 */
			for (j=0;j<LMSSIZE;j++) {
				L_temp = clean * BufTx[i-(LMSSIZE-1)+j];
				L_Acc = L_add(ec->Coeff[j]<<16, L_temp<<1);
				L_Acc = L_add(L_Acc, 0x00008000L);
				ec->Coeff[j] = L_Acc >> 16;
			}
	}


	/* Calculate RxMag2 and RxMax2 for the echo-removed Rx signal */
	/* Using the same window as in calculating TxMax */
	L_AccB = 0;
	for (i=0;i<N;i++) {			// RxMag2 = 31/32 RxMag2 + 1/32 |BufRx'[i]|
		L_Acc = (long)BufRx[i] << (16-3);
		L_Acc = L_abs(L_Acc);
		L_Acc = L_sub(L_Acc, (long)ec->RxMag2<<(16-3));
		L_Acc = L_add(L_Acc, (long)ec->RxMag2<<16);
		ec->RxMag2 = L_Acc >> 16;
		if (L_Acc > L_AccB)
			L_AccB = L_Acc;
	}
	ec->RxMax2 = L_AccB >> 16;


	/* Conditions to reduce adaptation rate */
	/* 1) Timer is more than 0.5 second */
	/* 2) echo cancellation attenuation > 24 dB */
	L_temp = (long)(ec->RxMag2 + 40) << 15;
	if ( ec->Timer > halfsecond
		|| LMSATTEN24*ec->RxMag1 > L_temp
		)
		ec->Shift = 1;	
	
	/* Done processing, return */
}


long subc(long acc, long sub)
{
    long nCheck, nRet;
    
    nCheck = acc - sub;
    if (nCheck >= 0)
    {
        nRet = (nCheck<<1) + 1;
    }
    else
    {
        nRet = acc<<1;
    }
    return nRet;
}

