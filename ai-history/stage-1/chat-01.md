# AI Chat History — Stage 1

- **Member:** Madu (Coder 2, Customer and Menu Domain)
- **AI tool:** Claude (Anthropic), claude.ai
- **Scope:** Stage 1 part of one continuous conversation. Stage 2 continues in `ai-history/stage-2/chat-01.md`, Stage 3 in `ai-history/stage-3/chat-01.md`.
- **Note:** Prompts and replies are reproduced in full and in order. Attachments are noted in square brackets. Background tool activity (the AI opening the attached files) is not shown.

---

## Prompt 1

[Attached: SC2002_Restaurant_Rush_Group_Assignment.docx, SC2002_Group_Project_Briefing.pdf]

> I'm sharing the project briefing and game assignment. For now, help me understand the requirements and brainstorm questions I should consider for Stage 1.
> First, summarize the Stage 1 requirements in your own words and point out anything that seems ambiguous or that I should confirm with my group or instructor. Then ask me questions that will help me form my own ideas about gameplay choices, possible design challenges, and how I might test the game. Keep requirements from the documents separate from your suggestions, and label any suggestions as brainstorming. I'll make the design decisions and check them against the PDFs myself.

## Reply 1

# Stage 1: requirements summary and questions

**Timing:** all three stages are submitted together. Stage 1 still needs its own runnable commit tagged `stage-1`.

## Part A: What the documents require (my summary)

**What Stage 1 must contain**
- A Java game played on the command line, with turns. It must compile and run by itself at the tag `stage-1`.
- Regular customers only, plus the three menu items: Salad ($9, 1 unit), Burger ($15, 1 unit) and Pasta ($18, 2 units).
- One Waiter, one Chef, one Cashier, two Tables, Orders and a waiting queue.
- The full turn sequence, including satisfaction, abandonment, payment, victory and defeat.
- No Stage 2 or 3 features. That means no VIP or Critic customers, no Host, no ComboMeal and no Happy Hour.
- Your test notes must include one victory run and one defeat run that someone else can reproduce.

**The fixed base setting**
- The game always runs 18 turns.
- One customer arrives at the start of turns 1, 4, 7, 10, 13 and 16.
- To win at the end of turn 18, you need at least 4 paid customers, at least $45 revenue, and an average satisfaction of at least 60 among paid customers.
- The turn-16 customer can never pay in time, so at most 5 customers can pay. The brief says this is intended.

**What happens in each turn**
1. **Arrival:** the scheduled customer joins the queue with satisfaction 100.
2. **Manager choices:** the Waiter does one task or waits. The task is seating one customer, taking one order, or serving one READY dish. The Chef advances one order by one unit or waits, and can work on an order the Waiter took this same turn.
3. **Kitchen and serving:** the Waiter acts before the Chef. A dish the Chef finishes this turn can be served next turn at the earliest.
4. **Eating and payment:** a customer served on an earlier turn finishes eating and becomes READY_TO_PAY. The Cashier then charges at most one READY_TO_PAY customer per turn, oldest first, and does this automatically. A customer who became READY_TO_PAY this turn pays next turn at the earliest. Payment adds the bill to revenue and frees the table.
5. **Waiting loss:** every customer who is waiting or seated but not yet served loses 8 satisfaction (minimum 0). A customer who reaches 0 leaves without paying. Their order is cancelled and their table is freed.
6. **Result:** shown only after turn 18. There is no early ending.

**Rules that always apply**
- Customers choose their own food. The rule must be deterministic or use seeded randomness, and the choice is shown once the order is placed.
- Game rules belong in domain and control classes, not in the UI.
- An invalid choice must explain why it failed and let the manager choose again. It does not use up the staff task.
- Revenue counts paid orders only. Money is stored as integer cents if fractional amounts appear.
- The game must work fully offline.
- The start-of-turn display has a required list of fields. So does the end-of-turn results display, which shows "N/A" for average satisfaction if nobody has paid yet.

**Requirements that come only from the PDF and affect you personally**
- Each member must upload their **complete, in-order AI chat history** to their own branch, for example `ai-history/stage-1/chat-01.md`. A summary is not enough. That probably includes this conversation, on your `member4` branch.
- Each member writes their own reflection section in the report.

## Part B: Things to confirm with your group or instructor

1. **One class diagram or three?** The assignment doc asks for one final class diagram that matches the Stage 3 code. The PDF asks for one diagram per stage, three in total. This is the biggest conflict between the two documents.
2. **Where is the line on "no Stage 2/3 features"?** The PDF says general support for future extensions is allowed. Would an abstract `Customer` class with an overridable satisfaction-loss method count as allowed groundwork or as a Stage 2 leak? What about storing money in cents in Stage 1?
3. **Display before or after arrival?** The display comes "at the start of every turn," and arrival is step 1. Does the manager see the customer who just arrived? The worked example (seated on the turn they arrive) suggests yes.
4. **Queue order.** Can the Waiter seat any queued customer, or only the one at the front? The brief doesn't say.
5. **Chef switching.** Can the Chef do unit 1 of one Pasta, switch to a different order, and come back later? Is partial progress kept?
6. **Customers still in the restaurant after turn 18.** They have neither paid nor left. Do they count as "unhappy departures," or are they reported separately?
7. **Format of the reproducible runs.** Is a written list of inputs in the test notes enough, or should the TA be able to run a script or feed a file of inputs?
8. **Satisfaction on the turn of serving.** The example shows no loss on the turn a customer is served (76 stays 76). Confirm the group's code checks "served" before applying the waiting loss.

## Part C: Brainstorming questions for you

*These are my prompts, not requirements.*

**Gameplay choices**
- What rule will customers use to pick food? A fixed cycle like Salad → Burger → Pasta, a seeded random pick, or something else? How does that rule affect whether victory is actually reachable?
- Since you're Coder 2 on customers and menu: the menu is fixed in Stage 1, but ComboMeal arrives in Stage 3. Should a menu item be an enum, a class, or something a combo can be built from?
- Is the game interesting with only two tables? Think about where the manager faces a real tension, such as seating a new customer versus serving a READY dish.
- How much information should the display give? Should it show how many turns each customer has before reaching 0, or leave the player to work it out?

**Design challenges**
- Which object should own each state change, such as an order becoming READY or a customer becoming READY_TO_PAY? Order, Customer, a staff object, or the turn controller?
- How will you keep the "can this be served yet?" timing rules out of the CLI?
- How will an invalid choice be reported without using up the task? An exception, a result object, or a boolean plus a message?
- What should Stage 1 code look like so that adding VIP in Stage 2 needs no chain of `instanceof` checks in the controller?
- Which customer and order statuses do you need? Are there more than READY, SERVED, READY_TO_PAY and PAID, for example WAITING, SEATED, ORDERED, CANCELLED and LEFT?

**Testing**
- Can you work out a winning sequence by hand first? The worked example costs one customer 24 satisfaction. What happens when two customers overlap?
- What is the quickest reliable way to lose? Doing nothing for 18 turns is one option, but does it show abandonment?
- Which edge cases are worth a dedicated test? Some ideas:
  - serving a dish on the same turn it became READY
  - seating a customer when both tables are full
  - a customer reaching exactly 0
  - two customers becoming READY_TO_PAY on the same turn
  - entering letters where a number is expected
- How will you prove the turn-16 customer behaves as intended?
- Could the game take its inputs from a file or a fixed seed, so the victory and defeat runs come out the same every time?

---

## Prompt 2

> Help me brainstorm possible Stage 1 gameplay situations where the manager's choices could affect the outcome. Give me a few high-level scenarios to think about, but don't choose rules or solutions for me.

## Reply 2

# Stage 1 scenarios where the manager's choices matter

*All of this is brainstorming. Each scenario is a situation to think through, not a rule or a recommended move. Whether a situation actually comes up will depend on rules your group still has to decide, such as how customers pick food.*

**1. A new arrival while a dish is READY**
A new customer has just joined the queue, a table is free, and a seated customer's dish is READY. The Waiter can do only one of these this turn.
- Which delay costs more: the new arrival's satisfaction or the seated customer's?
- Does serving first free up anything later, like a table, the Cashier, or the next order?

**2. Both tables full, the queue growing**
Two customers are seated somewhere in their meal and a third is waiting.
- How long can the waiting customer last before reaching 0?
- Is it better to rush one table toward payment, or to keep both moving together?

**3. Two orders, one Chef**
Two orders are waiting in the kitchen, perhaps a Pasta (2 units) and a Burger (1 unit).
- Does finishing the quick dish first help more than starting the longer one?
- If the Chef is allowed to switch between orders (one of the questions to confirm), does switching ever help, or does it just spread delay around?

**4. Taking an order versus seating someone**
One customer is seated without an order while another is still in the queue.
- Taking the order lets the Chef start, possibly this same turn. Seating the queued customer gets them closer to a table.
- How does the Waiter-before-Chef order within a turn change this choice?

**5. Saving a customer or letting them go**
One customer's satisfaction is low and they may never pay with a good score.
- Does serving them pull the paid-customer average below 60?
- Is it ever worth spending Waiter turns on someone else instead? How does that interact with the "at least 4 paid" target?

**6. The three win targets pulling against each other**
- Chasing revenue might mean favouring customers who ordered expensive dishes.
- Protecting average satisfaction might mean favouring whoever has waited longest.
- Getting 4 paid customers might mean speed over everything else.
- When do these conflict, and does the player get enough information to see the trade-off?

**7. The endgame**
In the last few turns, some customers can still finish paying and some can't.
- How would the manager know which customers are still worth working on?
- Is effort spent on the turn-13 customer wasted or decisive?

**8. A pile-up at the Cashier**
Two customers become READY_TO_PAY close together, and the Cashier charges one per turn.
- Does a slow table now hold up seating for the next arrival?
- Can the manager see this coming and change the serving order earlier?

**9. Using Wait on purpose**
- Is there ever a good reason for the Waiter or Chef to wait?
- If not, should the game show the player that waiting wasted a turn?

**10. A choice that looks fine now and hurts later**
- Is there a situation where the obvious move this turn leads to an abandonment or a missed payment several turns on?
- That kind of situation could be useful for the "manager choice changes an outcome" scenario the sequence diagram needs, and for the video segment showing a decision where staff are constrained.

**Questions to come back to after your group sets the rules**
- Do most turns have only one sensible move, or is there real tension? If every turn has an obvious answer, the game may feel scripted.
- Can a player who makes reasonable but imperfect choices still lose? Can a careful player reliably win?
- Which of these scenarios would be easiest to replay exactly for your victory and defeat test notes?

---

## Prompt 3

> The assignment requires a documented, deterministic or seeded-random rule for how customers choose food. Help me brainstorm a few high-level approaches I could consider. For each, briefly describe the trade-off,

## Reply 3

# How customers could choose food

*Brainstorming only. The requirement from the docs is just this: the rule must be deterministic or use seeded randomness, it must be documented, and the choice is shown once the order is placed. Each approach below meets that requirement. None of them is a recommendation.*

**1. Fixed cycle through the menu**
Customers take turns through the menu in order, for example Salad, Burger, Pasta, then back to Salad.
- *Pros:* the simplest to explain, test and replay. Anyone can predict every order.
- *Cons:* the player can memorise the order, so planning becomes trivial. The cycle also decides how much revenue is possible in total, which affects whether the $45 target is easy or tight.

**2. Fixed list in the arrival schedule**
Each of the six arrivals has its food written next to it, for example in a constant array or a config list.
- *Pros:* your group can design an exact scenario, such as making sure two Pastas collide in the kitchen. Good for writing victory and defeat runs on purpose.
- *Cons:* it can feel like a scripted puzzle rather than customers choosing. It's also worth asking whether this really counts as the customer choosing, or whether the brief means something more like behaviour.

**3. Seeded random choice**
A `Random` object created with a fixed seed picks an item when the order is taken.
- *Pros:* feels like a real choice. Changing the seed gives variety, and the same seed always gives the same game.
- *Cons:* only reproducible if the random calls happen in exactly the same order every run. One extra call anywhere changes every later result. You'd also have to check that the default seed actually allows a win.

**4. Seeded random with weights**
Same as 3, but some items are more likely than others, for example cheaper dishes picked more often.
- *Pros:* lets you tune difficulty and revenue without fixing each order.
- *Cons:* the weights need justifying in the report, testing is harder, and there are more numbers to keep consistent with the docs.

**5. A rule based on game state**
The choice depends on something already in the game, for example arrival number, the current turn, or how long the customer waited.
- *Pros:* deterministic but less obvious to the player. It can also create interesting interactions, like a customer who waited long ordering something quick.
- *Cons:* harder to explain, and it pushes game logic into the choice. Make sure the rule sits in the domain classes, not the UI. Also check that each rule only uses information the brief actually gives you.

**6. Each customer has its own preference**
Each customer object is given a preference when created, either from a list or from the seed, and orders from it.
- *Pros:* keeps the choice inside the customer, which may matter in Stage 2. Critics need "a distinct order preference," and the brief wants that dispatched through customer objects rather than type checks.
- *Cons:* more structure than Stage 1 strictly needs. Your group would also need to agree whether this counts as allowed groundwork or creeping into Stage 2.

**Questions that could help you choose**
- Which approach makes your victory and defeat runs easiest to reproduce exactly?
- Does the approach make the $45 revenue and 60 satisfaction targets reachable but still a challenge?
- Where in your design would the choice happen: in Customer, Order, Menu or a separate chooser? How easily could that place handle a Critic in Stage 2 and a ComboMeal in Stage 3?
- Would a TA reading your README be able to predict the orders from the documented rule alone?

Your message seemed to cut off after "describe the trade-off." If there was more you wanted, such as a comparison table, let me know.

---

## Prompt 4

> (customerId - 1) % 3 is this documented rule correct

## Reply 4

Yes. It meets the requirement: it's deterministic, uses no randomness, and is easy to document. Whether it's correct in your code depends on a few details you'd need to check yourself.

**What it gives**, assuming IDs start at 1 and the menu list is ordered Salad, Burger, Pasta:

| Customer ID | Arrives on turn | (id − 1) % 3 | Order |
|---|---|---|---|
| 1 | 1 | 0 | Salad |
| 2 | 4 | 1 | Burger |
| 3 | 7 | 2 | Pasta |
| 4 | 10 | 0 | Salad |
| 5 | 13 | 1 | Burger |
| 6 | 16 | 2 | Pasta (can't pay in time anyway) |

**What to check before calling it correct**
- **Where IDs start.** If IDs start at 0, `(0 - 1) % 3` is **−1** in Java, because `%` keeps the sign of the left number. Using that as a list index would crash. Make sure IDs really start at 1, or adjust the formula.
- **The menu order is part of the rule.** The formula only produces an index, so the documentation also has to say which item each index means. Use a fixed order, such as an enum's declared order, not something like a `HashMap` whose order isn't guaranteed.
- **When IDs are given out.** They should be assigned in arrival order and never reused. Then a customer who leaves doesn't shift anyone else's food.
- **Document the result as well as the formula.** A table like the one above in your README lets a TA predict each order without doing the maths.

**Things to think about (brainstorming)**
- **The revenue target is never the hard part with this rule.** Customers 1 to 5 together bring $66. Even the cheapest four of them (Salad, Burger, Salad, Burger) bring $48, which is above $45. So with this rule, any win comes down to getting 4 people paid with an average satisfaction of at least 60. That may be fine; just decide it knowingly.
- **There's one Pasta among the payable customers**, customer 3. Is that enough kitchen pressure to make the Chef's choices matter?
- **Later stages.** In Stage 2 you may swap two arrivals for a VIP and a Critic, so does the rule still use the same ID? In Stage 3, `% 3` never produces a ComboMeal, so the rule will probably need to change then. That's allowed, but it should be explained in your transition notes.

---

## Prompt 5

> can u explain what happens for the first few customer ids so i can check if i understand it right?

## Reply 5

Here's the calculation for the first four customers, step by step. It assumes IDs start at 1 and the menu list is in the order Salad (index 0), Burger (index 1), Pasta (index 2).

`%` gives the **remainder** after dividing. For example, `5 % 3` is 2, because 5 = 3 × 1 + 2.

**Customer 1** (arrives on turn 1)
- 1 − 1 = 0
- 0 % 3 = 0, because 0 divided by 3 is 0 with remainder 0.
- Index 0 → **Salad**

**Customer 2** (turn 4)
- 2 − 1 = 1
- 1 % 3 = 1, because 3 doesn't fit into 1, so the whole 1 is left over.
- Index 1 → **Burger**

**Customer 3** (turn 7)
- 3 − 1 = 2
- 2 % 3 = 2, for the same reason.
- Index 2 → **Pasta**

**Customer 4** (turn 10)
- 4 − 1 = 3
- 3 % 3 = 0, because 3 fits exactly once with nothing left over.
- Index 0 → **Salad** again. This is where the cycle restarts.

The pattern continues the same way: customer 5 gets index 1 (Burger) and customer 6 gets index 2 (Pasta).

**What the "− 1" is for:** it shifts the IDs so customer 1 lands on index 0. Without it, `1 % 3` is 1, so customer 1 would get Burger and the cycle would start in a different place.

**Quick check for you:** what index and dish would customer 7 get, if there were one? And what would happen if the first customer's ID were 0 instead of 1?

---

## Prompt 6

[Attached: screenshot of the Stage 1 role table — Coder 2 (Customer & Menu Domain), Member 2. Files: src/restaurant/model/customer/CustomerState.java, Customer.java, RegularCustomer.java; src/restaurant/model/menu/MenuItem.java, SingleMenuItem.java, Menu.java; src/restaurant/model/Table.java. Responsibilities: customer states (WAITING_QUEUE, SEATED, ORDERED, SERVED, READY_TO_PAY, PAID, ABANDONED); satisfaction decay (−8 per unserved turn) and departure cleanup at 0; base menu items (Salad $9.00/1u, Burger $15.00/1u, Pasta $18.00/2u) in integer cents; seeded / deterministic customer food ordering rule; table occupancy tracking and state binding.]

> this is my stage 1 role. can u explain what i need to think about first? don't give me code or the full solution

## Reply 6

Your part is the **data and rules about customers, the menu and tables**. The Waiter, Chef, Cashier and turn controller will all call into your classes, so their design depends on yours. That means the first things to think about are what your objects *mean* and what other people's code will *ask* of them, before any detail of how they work inside.

Here's an order to think in, with questions rather than answers.

## 1. The customer's life cycle (start here)

Your enum lists the states: WAITING_QUEUE, SEATED, ORDERED, SERVED, READY_TO_PAY, PAID and ABANDONED. Before coding, sketch them as a diagram with arrows between them.
- Which moves between states are allowed, and which must be impossible? For example, could a customer go from SEATED straight to SERVED?
- What causes each move: a Waiter task, the Chef, the end-of-turn step, or the Cashier?
- Is there a state for "eating"? Or does SERVED cover the turn of eating, followed by READY_TO_PAY? Check this against the worked example in Section 3.3 of the brief.
- Which states can lead to ABANDONED, and which can't? The brief says customers who are served, eating or ready to pay lose nothing.
- Who should be allowed to change a customer's state: anyone, or only through certain methods? This is your encapsulation evidence for the class diagram.

## 2. The boundaries with your teammates

Several of your responsibilities cross into other people's classes, so agree on these early. This is the most urgent part.
- **Order isn't in your file list**, but customer states and order states are closely linked. If a customer is ORDERED, what state is their order in? Who keeps the two consistent?
- **Departure cleanup** means cancelling the order and freeing the table. Does the customer do this itself, or does it report "I've reached 0" and let the controller tidy up? Which choice keeps game rules out of the wrong place?
- What will the start-of-turn display need from your classes, such as satisfaction, state and the table number? Who formats that for the screen? Remember the UI only displays; it shouldn't calculate.

## 3. Satisfaction decay

- Which states lose 8 points per turn and which don't?
- Where does the number 8 live? In Stage 2, VIPs lose 5 and Critics lose 12, and the brief says that difference must come from the customer objects, not type checks in the controller. Think about how your Stage 1 structure makes that change small, without adding Stage 2 features now.
- How is the minimum of 0 enforced? And how does the rest of the game find out that a customer reached 0?

## 4. The menu

- You have `MenuItem`, `SingleMenuItem` and `Menu`. Why is there a separate `SingleMenuItem`? You'll want a clear answer for the report. It looks like groundwork for ComboMeal in Stage 3, so be ready to say why that counts as allowed general support rather than a Stage 3 feature.
- Prices are stored in integer cents. Where does converting cents to "$9.00" for display happen, and who owns that?
- Can a menu item change after it's created? Should it be able to?

## 5. The food-ordering rule

- Which class owns the rule: `Customer`, `Menu`, or something else? Which choice would let a Critic in Stage 2 have its "distinct order preference" with the least change?
- Revisit the earlier checks: where IDs start, the fixed menu order, and when IDs are assigned.
- When exactly is the choice made: at arrival, or when the Waiter takes the order? The brief says the choice is *shown* once the order is placed.

## 6. Tables

- "State binding" is vague in your task list. Confirm with your group what it means. Is it simply that a table knows which customer is sitting there?
- When does a table become free? Make sure the two cases from the brief are covered: the customer pays, or the customer leaves.
- What should happen if code tries to seat someone at a table that's already taken? Remember the rule that an invalid choice must explain why it failed without using up the staff task. Who produces that explanation?

## 7. How you'll check your part

- Which situations would prove your classes are right? A few ideas:
  - one customer followed from 100 down to 0
  - a served customer not losing points
  - a table freed after payment or abandonment
  - each customer ID producing the expected dish
- Can you check these on your own, without the whole game working yet?

**A suggestion for where to start:** draw the state diagram from point 1, then go through point 2 with the teammates who own Order and the turn controller. Most of the remaining questions become easier once those two are agreed.

---

## Prompt 7

> what should i check to make sure it matches the stage 1 brief?

## Reply 7

Here's a checklist for your part, with each item tied to the section of the assignment doc it comes from, so you can check it against the PDFs yourself. It covers the brief's requirements only; anything that isn't in the docs is in a separate list at the end.

## Customers (Sections 3.2, 3.4, 3.5)

- [ ] **Arrival:** a new customer starts at satisfaction **100** and joins the waiting queue. *(3.2, step 1)*
- [ ] **Who loses points:** waiting customers and seated customers who haven't been served lose **8** points at **turn end**. *(3.2, step 5)*
- [ ] **Who doesn't:** customers who are served, eating or READY_TO_PAY lose nothing. *(3.2, step 5)*
- [ ] **Floor:** satisfaction never goes below 0. *(3.2, step 5)*
- [ ] **Leaving:** a customer who reaches 0 leaves without paying, any unfinished order is cancelled, and their table is freed. *(3.2, step 5)*
- [ ] **Eating:** a customer served on an earlier turn finishes one full turn of eating, *then* becomes READY_TO_PAY. *(3.2, step 4)*
- [ ] **Payment order:** a customer can't be charged before READY_TO_PAY, and one who became READY_TO_PAY this turn pays next turn at the earliest. *(3.2 step 4, 3.4)*
- [ ] **Recorded score:** the satisfaction counted in the average is the value **when the customer pays**. *(3.5)*
- [ ] **Abandoned customers** never add to revenue. *(3.4)*
- [ ] **Worked example:** trace one customer through the six turns in Section 3.3 and check your states and satisfaction match at every turn: 92, 84, 76, 76, 76, then paid with 76 recorded.

## Menu (Sections 2.1, 2.2)

- [ ] **Exactly three items with the right values:** Salad $9.00 / 1 unit, Burger $15.00 / 1 unit, Pasta $18.00 / 2 units. *(2.1)*
- [ ] **Money:** stored as integer cents and displayed as dollars with two decimals. *(2.2)*
- [ ] **No ComboMeal or discounts** of any kind in Stage 1. *(4.1)*

## Food-ordering rule (Section 2.2)

- [ ] **The customer chooses,** never the manager. *(2.2)*
- [ ] The rule is **deterministic or seeded**, and **documented** in the README or report. *(2.2)*
- [ ] The choice is **shown once the order is placed**. *(2.2)*

## Tables (Sections 2.1, 3.4)

- [ ] **Exactly 2 tables** in the base setting. *(2.1)*
- [ ] A table **stays occupied until its customer pays or leaves**. *(3.4)*
- [ ] **Seating at a full table, or with no free table,** produces an explanation, and the manager can choose again without using up the Waiter's task. *(3.4)*

## Design rules that apply to your classes (Sections 2.2, 4.1, 4.4)

- [ ] **No game rules in the UI.** Decay, leaving, state changes and the ordering rule all live in your domain classes or the controller, never in the CLI. *(2.2)*
- [ ] **No Stage 2 or 3 features:** no VIP or Critic classes or values, no Host, no ComboMeal, no Happy Hour. *(4.1)*
- [ ] **No interfaces or abstractions added only to tick a box.** You should be able to explain why `MenuItem`, `SingleMenuItem` and `Customer`/`RegularCustomer` each exist. *(4.4)*
- [ ] **It compiles and runs at the `stage-1` tag** together with your teammates' code. *(4.1)*

## Not in the brief, but worth checking (brainstorming)

- Does every state in your enum match a situation the brief describes? Is any situation from the brief missing a state?
- Can any code put a customer into a state the brief doesn't allow, such as straight from SEATED to SERVED?
- Do the customer's state and their order's state ever disagree? That's something to check with whoever owns Order.
- Does customer ID 1 really get index 0 in your rule?
- Will the start-of-turn display be able to get everything it needs from your classes, such as satisfaction, state and the table each customer is at?
