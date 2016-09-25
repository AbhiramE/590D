import pylab

def monty_hall():
	from random import randint
	a='Player selects Door 1'
	b='Player switches to Door 2'
	winner=None
	winCount=0
	winCounts=[]

	for n in xrange(0,1100,100):
		for i in xrange(1,n):
			roll=randint(1,3)		#Randomize to win with probability of 2/3
			if roll==1 or roll==2:
				winner=b            #Player wins on Switching doors
			else:
				winner=a            #Player wins on Sticking with Door 1
			if winner==b:
				winCount+=1
		winCounts.append(winCount)

	pylab.figure(1)
	pylab.title('Win vs Trails plot for Monty Hall on switching doors')
	pylab.xlabel('Number of Trials')
	pylab.ylabel('Number of Wins')
	pylab.plot(xrange(0,1100,100),winCounts,'--bo')
	pylab.show()

monty_hall()

