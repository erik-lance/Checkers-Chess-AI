# Checkers-Chess-AI
A project for the requirements of CS Intelligent Systems that lets a human play against an AI in Checkers and Chess. The agent uses Game Trees through Min/Max and Alpha-Beta Pruning.

Constructing the Alpha-Beta Code was easy enough, but it appears during my development process, what was difficult was constructing the game itself. It might have been better to start with a proper UML diagram before coding.

## Tree
The heuristics are calculated under the node. This is to help provide a static evaluation if this is the last node in a tree. The prepareTreeAtDepth function was a bit daunting to calculate. What happens is it first creates children under the root node, and then while looking at the root node, creates children for each child of the root node. It repeats the process as it scans through each node. Let's say the depth is 3, it will only look until node at depth 1. This is because it creates children for each child of the selected node.

## Minimax
The agent is the maximizer and the player is the minimizer. Basically, at each depth, if the agent plays the best move, it assumes the human plays the best move, and finds the best move after (basically this is what happens at depth 3). Move ordering with A-B pruning is also implemented to reduce load time. Speaking of load time, I added a 100ms delay in each move. Anything longer is mostly machine processing time. This is measured with the "Nodes Traversed" print.

## Heuristics
When it comes to checkers, the utility function or heuristic calculation is actually simple. We use a static material evaluation to calculate the best play. It simply counts all available pieces, but kinged pieces are valued higher by 1 point. The agent's number of pieces are then subtracted to the human's number of pieces. It simply stays at zero until there's an uneven takeback of pieces.

However, the chess heuristics is a different story. It is still quite similar in Checkers heuristics with material static evaluation, however, I took the liberty to look up the PeSTO evaluation which is used in RofChade. It's too complicated for me to explain, but I used their evaluation function for the sake of this project. It is not working as fully intended, but works pretty well for an AI nonetheless.
