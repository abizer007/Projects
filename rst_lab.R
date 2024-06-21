#SM 11 dec 23
1+2
2*369
2*3-6+5
59/66
3/2
3%/%2 #for integral answer.
2^3
sqrt(5)

v1=c(1,2,3)
v1
v2=c(4,5,6)
v2
v1+v2
v1*v2
sum(v1*v2)
v1/v2
v1%/%v2
v1%*%v2
x = c(65,66,67,67,68,69,70,72)
y = c(67,68,65,68,72,72,69,78)
a =sum(x*y)/8
b = (sum(x)/8)*(sum(y)/8)
c = (sum(x^2)/8)-(sum(x)/8)^2
d = (sum(y^2)/8)-(sum(y)/8)^2
r = (a-b)/sqrt(c*d)
r

#spearsons method
acc=c(45,70,65,30,90,40,50,57,85,60)
stat=c(35,90,70,40,95,40,60,80,80,50)
rank(-acc)
rank(-stat)
d=rank(-acc)-rank(-stat)
View(data.frame(acc,rank(-acc),stat,rank(-stat),d,d^2))
n= length(acc)
m1=2 
m2=2
rt=((m1^3-m1)+(m2^3-m2))/12
#spearman's rank correlation formula is:
#r=1-((6*sum(d^2)/(n^3-n))
rS=1-((6*sum(d^2)+rt)/(n^3-n))
rS      

#line 
x=c(2,4,6,8)
y=c(5,7,8.5,11)
sum(x)
sum(y)
sum(x*y)  
plot(x,y,col="red")
yl=0.975*x+3
plot(x,yl,type="l")
points(x,y,col="red")

#Matrices

A=matrix(c(1,8,9,3),nrow= 2)
A
t(A)
det(A)
A==t(A)
-A==t(A)
A^2
A^3
s=matrix(c(0,-2,2,0),nrow=2)#skew symmetric matrix proving
-s==t(s)
B=matrix(c(1,2,4,2,1,3,4,3,1),nrow=3)#symmetric matrix
B==t(B)

Y=matrix(c(4,8,36,3,11,7,2,13,5),nrow=3)
det(Y)
C=matrix(c(10,19,8),nrow=3)
solve(Y,C)

#8/1/2024
#csbs
x=c(2,4,6,8)
y=c(5,7,8.5,11)

View(data.frame(x,y,yhat,err,err^2))
yhat=3+0.975*x #predicted value of y
err=y-yhat
sum(err^2)

x=c(45,70,65,30,90,40,50,57,85,60)
y=c(35,90,70,40,95,40,60,80,80,50)
View(data.frame(x,y))
plot(x,y,col="red")
sum(x)
sum(y)
sum(x*y) 
yl=plot(x,yl,type="l")
points(x,y,col="red")

yhat2=5.35+0.9907*x
err=yhat2-y
sum(err)
sum(err^2)
points(x,y,col="red")

#data type mtcars

data("mtcars")
View(mtcars)
y=mtcars$hp
x=mtcars$disp

lm1=lm(mtcars$hp ~mtcars$disp)
lm1
lm2=lm(mtcars$hp ~mtcars$disp+mtcars$wt)
lm2
lm3=lm(mtcars$hp ~mtcars$disp+mtcars$wt+mtcars$qsec)
lm3
plot(lm1,lm2,lm3,col="red")
points(lm1,lm2,lm3)
tv=c(8.6,23.8,204.1,218.4,66.9,199.8,100.4,216.4,182.6,262.7)
sales=c(2.1,35.1,32.9,27.7,11.7,3.1,9.6,41.7,46.2,28.8)
newspaper=c(1.0,63.9,46.0,53.4,36.8,34.6,3.6,29.6,58.7,15.9)
lm1=lm(sales ~tv+newspaper)
lm1
plot(lm1,col="red")

#15/1/2024
#LAB 4
#Dataframes

names=c("Ayush","Rohan","chetransh","kamlesh","raju","taha")
roll=c(1,2,3,4,5,6)
cars=c("honda","Nissan","Mercedes","Mclaren","Aston","Fiat")
colors=c("Blue","Black","white","Magenta","purple","Red")

dat1=data.frame(names,roll,cars,colors)
dat1
View(dat1)
summary(dat1)

#Accessing components of dataframe
#dat1[row,column]
dat1[1]
dat1[1:2]
dat1[-1]
dat1[4]
dat1[1:3]
dat1[1:2]
dat1[1,3]
dat1[1:4,2]
dat1[1,1:4]
dat1[2,1:4]
dat1[1:3,1:4]
#CSV file importing

dat2=read.csv("/Users/mpstme.student/Downloads/advertising.csv")
dat2
View(dat2)
cor(dat2$Sales,dat2$TV)
cor(dat2$Sales,dat2$Newspaper)
cor(dat2$Sales,dat2$Radio)

lm1=lm(dat2$Sales~dat2$TV)
lm1
lm2=lm(dat2$Sales~dat2$Newspaper)
lm2
lm3=lm(dat2$Sales~dat2$Radio)
lm3
lm4=lm(dat2$Sales~dat2$TV+dat2$Radio)
lm4
lm5=lm(dat2$Sales~dat2$TV+dat2$Radio+dat2$Newspaper)
lm5
summary(lm1)
summary(lm2)
summary(lm3)
summary(lm4)
summary(lm5)
#Probability distridutions
#Binomial distribution
n=250
x=0:n
p=0.5
q=1-p
pb=choose(n,x)*(p^x)*(q^(n-x))
pb
View(data.frame(x,pb))
plot(x,pb)

#inbuilt
pbb=dbinom(x,size = 250,p=0.5)
plot(x,pbb)

pbc=cumsum(pbb)
#cumsum functions gives value adding all the value one by one and not together
#q means upto which number you want cumulative sum.
plot(x,pbc)

#poisson distribution
#p(X=x)=e^-lamda*lamda^x/x!
n=25
x=0:25
lam=4
pp=(exp(-lam)*(lam^x))/(factorial(x))
plot(x,pp)

ppp=dpois(x,lambda = 4)
plot(x,ppp)

#19/02/24
#lab 5

#Run test
#H0: Sequence is random
#H1: Sequence isnt random
r = 10  
n1 = 42
n2 = 17
u = ((2*n1*n2)/(n1+n2)) + 1
s = sqrt(((2*n1*n2)*((2*n1*n2) - n1 - n2))/(((n1 + n2)**2)*(n1 + n2 - 1)))
z = (r - u)/s
View(z)    
if (z > 1.96){
  print("H0 is accepted")
}
{
  print("H0 is rejected")
}

#Mann WHitney U test
x = c(7,5,6,4,12)
y = c(3,6,4,2,1)
n1 =  5
n2 = 5
z = c(x,y)

rankt = rank(z)
r1 = sum(rankt[1:length(x)])
r2 = sum(rankt[length(x)+1:length(y)])
data.frame(x,y,r1,r2)
u1 = n1*n2 + ((n1*(n1 + 1))/2) - r1
u2 = n1*n2 + ((n2*(n2 + 1))/2) - r2
u = min(u1,u2)
print(r1)
print(r2)
print (u)

#4/03/24
#lab 6
#spearman kendal:-
x=c(5,6,8,3,2,5)
y=c(6,5,9,4,2,5)
n=length(x)
m1= 2
m2= 2
n
r1 = rank(x)
r2 = rank(y)
di = sum(r1-r2)
di2 = sum((r1-r2)^2)
rn = 6*(di2 + sum((m1^3-m1)/12 + (m2^3-m2)/12))
rd = n^3-n
r = 1 - rn/rd
r = cor(x,y,method="spearman")
r
t = r*sqrt((n-1)/(1-r^2))
t

#if - statement
#1)
x <- 5
if(x > 0){
  print("Positive number")
}

#2)
x <- 5 
if(x > 0){ print("Positive number") } 
else{ print("Negative number") }

#3)
a <- 67
b <- 76
c <- 99
if(a > b && b > c)
{
  print("condition a > b > c is TRUE")
}else if(a < b && b > c)
{
  print("condition a < b > c is TRUE")
}else if(a < b && b < c)
{
  print("condition a < b < c is TRUE")
}

# while loop - statement
#1)
num =0 
while(num< 5){ 
  print(paste("num =", num)) 
  num = num+1
} 
i <- 1 
while (i < 6) {
  print(i) 
  i = i+1 
}

# for statement
#1)
x <- c(2,5,3,9,8,11,6) 
count <- 0 
for (val in x) { 
  if(val %% 2 == 0) 
    count = count+1 
}
print(count)

#2)
v <- LETTERS[1:4] 
for ( i in v) { 
  print(i) 
}

#Repeat loop
#1)
v <- c("Hello") 
cnt <- 2 
repeat { 
  print(v) 
  cnt <- cnt+1 
  if(cnt > 5) {
    break 
  } 
}
#break statement
x <- 1:5 
for (val in x) { 
  if (val == 3){
    break 
  } 
  print(val) 
}

# Fuction
circle = function(r){
  A=2*pi*r^2
  print(A)
  B=2*pi*r
  print(B)
}
circle

#spearman kendal function
x=c(5,6,8,3,2,5)
y=c(6,5,9,4,2,5)
r=cor(x,y,method="spearman")
r
SK=function(r,n,ttab){
  tcal=r*sqrt((n-2)/(1-r^2))
  print(r)
  print(ttab)
  print(tcal)
  if(abs(tcal)<abs(ttab)){
    print("Null hypothesis is accepted")
  }else{
    print("Null hypothesis is rejected")
  }
}
SK(0.8676471,6,1.96)

#11-03-2024
#lab 7

#run test function

RT =function(r,n1,n2){
u = ((2*n1*n2)/(n1+n2)) + 1
s = sqrt(((2*n1*n2)*((2*n1*n2) - n1 - n2))/(((n1 + n2)**2)*(n1 + n2 - 1)))
z = (r - u)/s
print(z)
print(u)
print(s)
if (z > 1.96){
  print("H0 is accepted")
}
{
  print("H0 is rejected")
}
}
RT(6,20,10)

#Mann Whitney U test function

MW = function(x,y,n1,n2,utab){
z = c(x,y)
rankt = rank(z)
r1 = sum(rankt[1:length(x)])
r2 = sum(rankt[length(x)+1:length(y)])
data.frame(x,y,r1,r2)
u1 = n1*n2 + ((n1*(n1 + 1))/2) - r1
u2 = n1*n2 + ((n2*(n2 + 1))/2) - r2
u = min(u1,u2)
print(r1)
print(r2)
print (u)

if (u < utab)
  print("Ho is rejected")
else
  print("Ho is accepted")
}
x = c(7,5,6,4,12)
y = c(3,6,4,2,1)

MW(x,y,5,5,2)

#18-03-2024
#lab 8

# parametric inference
# z - test
# T - test
# F - test

# Non-parametric inference
# Run - test
# Spearman kendal - test
# Mann whitney U - test
# kai square - test
# kai square attributes - test
# KS - test
# wilcoxin test
# sign - test

#F test - function

FT = function(n1,n2,s1,s2,ftab){
  sc1 = (n1*s1)/(n1-1)
  sc2 = (n2*s2)/(n2-1)
  fcal = sc1/sc2
  if (sc1 < sc2){
    fcal = sc2/sc1
  }
  print(fcal)
  if (fcal < ftab){
    print("We accept H0")
  }
  else{
    print("We reject H0")
  }
}
FT(8,7,4.1875,3.3877,4.206)

# T test - Function

TT = function(x1,x2,n1,n2,s1,s2,ttab){
  tn = x1-x2
  td = sqrt((n1*s1^2)+(n2*s2^2)/(n1+n2-2)*(1/n1+1/n2))
  tcal = tn/td
  if (tcal < ttab){
    print("we accept H0")
  }
  else {
    print("We reject H0")
  }
}
TT(9.6,16.5,8,11,1.2,2.5,2.26)

# Z test - Function

ZT = function(n,x,u,s,ztab){
  zn = x-u
  zd = s/sqrt(n)
  z = zn/zd
  print(z)
  if (z < ztab){
    print("we accept H0")
  }
  else {
    print("we reject H0)")
  }
}
ZT(100,160,165,10,1.96)

# Z test one sample proportion function
ZTP = function(p,s,n,ztab){
  q = 1-s
  zn = p - s
  zd = sqrt(s*q/n)
  zcal = zn/zd
  print(zcal)
  if (zcal<ztab){
    print("We accept H0")
  }
  else {
    print("We reject H0")
  }
}
ZTP(0.125,0.2,400,1.96)

# Z test two sample proportion function
ZTPP = function(p1,p2,s,n1,n2,q,ztab){
  zn = p1-p2
  zd = sqrt(s*q(1/n1+1/n2))
  z = zn/zd
}



















