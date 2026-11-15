# AI Chat History — Stage 3

- **Member:** Madu (Coder 2, Customer and Menu Domain)
- **AI tool:** Claude (Anthropic), claude.ai
- **Scope:** Stage 3 part of one continuous conversation. It follows `ai-history/stage-1/chat-01.md` and `ai-history/stage-2/chat-01.md`.
- **Note:** Prompts and replies are reproduced in full and in order. Attachments are noted in square brackets. Background tool activity (the AI generating a PDF file) is not shown.

---

## Prompt 14

> can u suggest a possible role split for stage 3 across our group?

## Reply 14

This split keeps the same five roles your group used for Stage 2, so everyone stays in the area they already know. It's my suggestion, not a requirement, so adjust it to your group.

## What Stage 3 has to add (brief Section 4.3)

- **Host:** seats at most one queued customer per turn, in addition to the Waiter's task. The Waiter can still seat customers too.
- **ComboMeal:** made of two existing menu items. Its preparation units are the two items' units added together, and its price is 90% of their combined price, rounded to the nearest cent. Customers choose combos by a rule you document.
- **Happy Hour:** the manager can switch it on once per game, at the start of a turn. It lasts that turn and the next, and its remaining duration must be shown. While it's active:
  - Orders taken get 20% off. The VIP discount is applied first, then Happy Hour. The price is locked when the order is taken.
  - Customers still waiting for a table or food recover 15 satisfaction at the start of the turn, up to 100.
- Stage 3 must be playable on its own and tagged `stage-3`.

## Suggested split

| Role | Possible Stage 3 work |
|---|---|
| **Coder 1** (engine) | Add the Host's step to the turn order. Add the Happy Hour timing: when it can be switched on, its two-turn length, the once-per-game limit, and when the +15 recovery happens. Keep every Stage 1 and 2 rule unchanged. Merge everything and tag `stage-3`. |
| **Coder 2** (customer and menu, you) | ComboMeal: its units, its price, and how it fits alongside the single menu items. The documented combo-choosing rule, including whether a Critic picks combos differently. The satisfaction recovery capped at 100 on the customer. Make sure the VIP discount combines correctly with Happy Hour. |
| **Coder 3** (staff, order, CLI) | The Host staff class and its input prompt. Locking the order's price when it's taken. The CLI prompt to switch on Happy Hour and the display of its remaining duration. Showing combos when an order is placed. Re-testing invalid input for the new choices. |
| **Non-Coder 1** (UML) | The Stage 3 class diagram. The sequence diagram: the brief's own example, VIP pricing together with Happy Hour, would fit well. The README comparison for Stage 2 → 3. A design discussion section covering the principles and one alternative design. |
| **Non-Coder 2** (testing) | Stage 3 victory and defeat runs with exact inputs. The brief's own worked numbers as checks: Salad + Pasta combo = $24.30 and 3 units; a VIP Burger during Happy Hour = $10.80. Also check: Happy Hour can't be switched on twice; recovery stops at 100; orders placed earlier keep their price. Audit `ai-history/stage-3/`. Collect the video timestamps. |

## Decisions to make together first

As with Stage 2, agree these before splitting off, because they cross between roles:

1. **Rounding when discounts stack.** A VIP ordering a Salad + Pasta combo during Happy Hour gives 2430 × 0.9 × 0.8 = **1749.6 cents**. Do you round once at the end, or after each discount? The brief only gives the rounding rule for the combo price itself.
2. **Where the price calculation lives.** Customer, Order and the Happy Hour setting all affect the final price. Who works it out, and at what moment?
3. **The combo-choosing rule.** Which combos exist? Can a combo contain the same item twice? Who orders combos?
4. **Host versus Waiter order.** Does the Host seat customers before or after the Waiter acts? The brief doesn't say.
5. **Recovery and arrivals.** Does a customer who arrives on a Happy Hour turn get the +15 recovery?

## Still to do overall

- The group report with each member's reflection
- The 15-minute video
- The README links to each stage and to every member's AI history
- The signed declarations

Try to give these named owners now rather than leaving them until the end.

Want me to turn this into a doc you can share with your group?

---

## Prompt 15

> can u suggest a possible role split for stage 3 across our group? in a pdf

## Reply 15

[The AI generated a 3-page PDF, Stage_3_Team_Role_Allocation_Matrix.pdf, laid out like the Stage 2 matrix and marked as a draft suggestion. It contained: a summary of the Stage 3 requirements from brief Section 4.3; a role allocation table with suggested files and responsibilities for each of the five roles; the five "decisions to confirm" listed in Reply 14; handoff rules (branch from stage-2, agree the pricing hand-off first, merge order Member 2 → Member 3 → Member 1 → documents, list every changed file in the README); and a final submission task table with a blank owner column. The reply text was:]

I've made the Stage 3 role split as a 3-page PDF, Stage_3_Team_Role_Allocation_Matrix.pdf, laid out like your Stage 2 matrix. It's marked as a draft suggestion for your group to discuss. Roles stay in the same areas as Stage 2:

- **You (Coder 2):** the ComboMeal and the rule for which customers order combos. Also the +15 satisfaction recovery (capped at 100), and checking that the VIP discount combines correctly with Happy Hour.
- **Coder 1:** fitting the Host and the Happy Hour timing into the turn order, then merging and tagging `stage-3`.
- **Coder 3:** the Host class, locking each order's price when it's taken, and the new Happy Hour prompt and displays.
- **Non-coders:** the Stage 3 class diagram, the sequence diagram, the README comparison, and the Stage 3 test runs.

**The file names in it are my guesses** based on the folder structure in your Stage 2 matrix, so rename them to match your actual code.

**Agree the open decisions before splitting the work.** The most important is rounding when discounts stack. A VIP ordering a Salad + Pasta combo during Happy Hour comes to 1749.6 cents, and the brief doesn't say whether to round once at the end or after each discount.

The last page lists the final submission tasks with a blank owner column. It's worth filling that in with your group.

---

## Prompt 16

> i'm coder 2. what should i think about for my stage 3 part? give me ideas

## Reply 16

Your Stage 3 work has three parts: the **ComboMeal**, how customers **choose** a combo, and making sure the **price and recovery** rules work properly with your customer classes. Everything below is brainstorming unless it's marked as coming from the brief.

## 1. The ComboMeal itself

**What the brief fixes:** a combo is made of two existing menu items. Its preparation units are the two items' units added together. Its price is 90% of their combined price, rounded to the nearest cent.

- **How it fits with your menu classes.** You already have `MenuItem` and `SingleMenuItem`. Can a ComboMeal be used anywhere a `MenuItem` is used, by the Chef, the Order and the display, without anyone needing to know it's a combo? That's a strong polymorphism and LSP example for the report.
- **What a combo is built from.** Any two `MenuItem`s, or only single items? Could a combo ever contain another combo? Should that be prevented, and where?
- **Which combos exist.** Every possible pair, or a fixed set registered in `Menu`? Can a combo repeat an item, like Burger + Burger?
- **The name it shows,** for example "Salad + Pasta Combo" in the order line.
- **A quick check worth doing yourself:** work out 90% of each possible pair in cents. Do any of them actually need rounding on their own? The answer affects where rounding really matters.

## 2. How customers choose combos

**What the brief fixes:** customers choose a combo by a rule you document.

- **Who orders combos?** Every customer sometimes, only certain IDs, or only certain types?
- **Your Stage 1 rule `(id − 1) % 3` only covers three items.** Do you extend it, replace it, or add a separate step that decides "combo or single item"? Whatever changes from Stage 2 needs a reason in the README.
- **The Critic's "always Pasta" preference.** Does it still apply in Stage 3? Could a combo that includes Pasta count? Its documented behaviour shouldn't quietly change.
- **Game balance.** Combos take 2 to 4 preparation units, so they put more pressure on the Chef. With the arrival schedule, can a victory run still work?
- **Reproducibility.** Can Member 5 predict every customer's order from your documentation alone?

## 3. Pricing: the trickiest part

**What the brief fixes:** the VIP discount is applied first, then Happy Hour. The price is **locked when the order is taken**. Orders already placed don't change.

- **When does `calculateFinalBill()` run right now?** If it runs when the customer pays, Happy Hour may already be over by then, and the price wouldn't be locked when the order was taken. This could be the biggest design change in Stage 3, so check it early.
- **Who does which part?** Your customer classes know about the VIP discount. The order (Coder 3) stores the locked price. Happy Hour (Coder 1) knows whether it's active. How does that information reach the place where the price is worked out, without the customer needing to know game rules it shouldn't?
- **Rounding when discounts stack.** Agree with the group whether to round once at the end or after each step. The VIP Salad + Pasta combo during Happy Hour (1749.6 cents) is the case that shows the difference.
- **Check against the brief's examples:** Salad + Pasta combo = $24.30 and 3 units; VIP Burger during Happy Hour = $10.80.

## 4. Satisfaction recovery

**What the brief fixes:** during each active Happy Hour turn, customers still waiting for a table or food recover 15 at the start of the turn, up to a maximum of 100.

- **Which states count as "waiting for a table or food"?** Look at your state list from Stage 1: is it the same group of customers who lose points?
- Is this the same for every customer type, or should VIPs and Critics differ? The brief doesn't mention differences.
- Where is the maximum of 100 enforced? Is it in the same place as the minimum of 0?
- Who triggers the recovery? Probably the engine, calling a method on the customer, in the same way the satisfaction loss works.

## 5. Not breaking Stages 1 and 2

- Do Regular, VIP and Critic customers still get exactly the same results for single dishes as before?
- If any method signature changes, for example `calculateFinalBill` now needing Happy Hour information, who calls it, and does their code still compile?
- Is every change you make to an earlier class one you can explain in a sentence for the README?

**Where to start:** decide point 3 with Coders 1 and 3 first. The pricing hand-off affects all three of you, while the ComboMeal and combo-choosing rule are mostly yours to decide alone.

---

## Prompt 17

> checklist for stage 3

## Reply 17

Here's a Stage 3 checklist for your role, set out like the Stage 2 one. **[Brief]** items come from the assignment doc (Section 4.3 unless noted). **[Draft]** items come from the Stage 3 split I suggested, which your group hasn't confirmed, so check those against what you all actually agree.

## Before coding

- [ ] The pricing hand-off is agreed with Coders 1 and 3: who calculates the locked price, and how the code knows Happy Hour is active. *[Draft]*
- [ ] The rounding approach for stacked discounts is agreed: once at the end, or after each discount. *[Draft]*
- [ ] Your branch starts from `stage-2`, and that tag is never moved. *[Draft; Brief Section 5]*

## ComboMeal

- [ ] Made of **two existing menu items**. *[Brief]*
- [ ] Preparation units = the **sum** of the two items. *[Brief]*
- [ ] Price = **90%** of their combined price, **rounded to the nearest cent**. *[Brief]*
- [ ] Stored as **integer cents** and shown as dollars with two decimals. *[Brief 2.2]*
- [ ] Salad + Pasta gives **$24.30 and 3 units**. *[Brief example]*
- [ ] Works anywhere a normal menu item does, for the Chef, the Order, the Cashier and the display. *[Draft]*

## Choosing combos

- [ ] Customers choose combos by a **documented** rule. *[Brief]*
- [ ] That rule is still **deterministic or seeded**, and the choice is shown when the order is placed. *[Brief 2.2]*
- [ ] The Critic's documented preference still holds, or the change is explained. *[Brief 4.2, 4.4]*
- [ ] The manager never chooses the food. *[Brief 2.2]*

## Pricing

- [ ] The **VIP discount is applied first, then Happy Hour's 20%**. *[Brief]*
- [ ] A VIP Burger during Happy Hour gives **$10.80**. *[Brief example]*
- [ ] The price is **locked when the order is taken**. *[Brief]*
- [ ] Orders placed before Happy Hour keep their price. *[Brief]*
- [ ] Customers who left aren't affected. *[Brief]*
- [ ] Revenue still only includes paid orders. *[Brief 3.4]*

## Satisfaction recovery

- [ ] Customers still **waiting for a table or food** recover **+15** at the start of each Happy Hour turn. *[Brief]*
- [ ] Satisfaction never goes **above 100**. *[Brief]*
- [ ] Customers who are served, eating or ready to pay don't recover. *[Brief]*

## Not breaking Stages 1 and 2

- [ ] Regular, VIP and Critic customers keep their Stage 2 loss rates and bills for single dishes. *[Brief 4.2]*
- [ ] Still **no type checks** in the controller. *[Brief 4.2]*
- [ ] No game rules in the UI. *[Brief 2.2]*
- [ ] Every change to an earlier class has an explained reason. *[Brief 4.4]*
- [ ] Stage 3 compiles and runs on its own, tagged `stage-3`. *[Brief 4.3, Section 5]*

## Evidence

- [ ] Every new or changed file is listed for the README with its reason. *[Brief Section 5]*
- [ ] Javadoc states the combo price rule and the combo-choosing rule. *[Draft]*
- [ ] Your values agree with Member 5's checks:
  - the two worked examples from the brief
  - recovery stopping at 100
  - earlier orders keeping their price

  *[Draft]*
- [ ] Your AI chat is saved under `ai-history/stage-3/`. *[briefing PDF]*

## Worth checking too (brainstorming)

- Does `calculateFinalBill()` still run at payment time? If so, does locking the price at order time still work?
- Do any combo prices need rounding on their own, or only when discounts stack?
- With combos in play, can a victory run still be won with the Stage 3 arrival schedule?
- Is the recovery's maximum of 100 enforced in the same place as the minimum of 0?

---

## Prompt 18

> what trade-offs should i think about for my stage 3 feature?

## Reply 18

These are all brainstorming. Each trade-off sets out two directions so you can weigh them. None is a recommendation, and any of them could become the "trade-offs and one alternative design" part of your report's design discussion.

## 1. How ComboMeal relates to the other menu items

- **A combo is a kind of `MenuItem`**, so it can be used anywhere a single dish can.
  - *Gain:* the Chef, the Order and the Cashier don't need to change, and it makes a clear polymorphism example.
  - *Cost:* the `MenuItem` abstraction has to suit both a single dish and a pair of dishes, which may make it more general than it needs to be.
- **A combo is its own separate type**, which other code handles differently.
  - *Gain:* simpler to understand on its own.
  - *Cost:* other classes would need to know about combos, which means more changes to earlier code and possibly type checks.

## 2. Which combos exist

- **A fixed set registered in `Menu`.**
  - *Gain:* predictable, easy to test and document.
  - *Cost:* less flexible, and the choice of which pairs to include needs explaining.
- **Any pair can be made on demand.**
  - *Gain:* general and complete.
  - *Cost:* more cases to test, such as repeated items and the highest-unit combos, and it's harder for the player to keep track of.

## 3. Working out a combo's price and units

- **Calculated once when the combo is created.**
  - *Gain:* simple and fast, and the value never shifts.
  - *Cost:* if a component's price ever changed, the combo wouldn't follow. That probably doesn't matter in this game.
- **Calculated from the two components each time it's asked for.**
  - *Gain:* always consistent with the components.
  - *Cost:* the rounding happens on every call, so you need to be sure it gives the same answer every time.

## 4. The combo-choosing rule

- **Extend the existing rule**, for example a bigger cycle that now includes combos.
  - *Gain:* one rule to document.
  - *Cost:* the single-dish choices Regular customers made in Stages 1 and 2 may change, which you'd need to explain.
- **Add a separate "combo or not" decision first**, then keep the old rule for single dishes.
  - *Gain:* earlier behaviour stays recognisable.
  - *Cost:* two rules to document and test.
- **Keep it simple, or make it interesting?** A simple rule is easy to reproduce. A clever one gives the player more to think about, but is harder to explain and harder for Member 5 to test.

## 5. Where the final price is calculated

- **In the customer**, which applies the VIP discount and then Happy Hour.
  - *Gain:* keeps the "behaviour comes from the customer" pattern from Stage 2.
  - *Cost:* the customer then needs to know about Happy Hour, which is a restaurant-wide setting rather than something about the customer.
- **In the order or a pricing object**, which asks the customer for its discount and checks Happy Hour separately.
  - *Gain:* each class has one clearer job.
  - *Cost:* more coordination with Coders 1 and 3, and changes to code that was settled in Stage 2.
- **Calculated when the order is taken, or when the customer pays?** Taking it at order time matches the brief's "locked" rule. If your Stage 2 code works it out at payment, switching means changing earlier code. That's justified, but it needs explaining.

## 6. Rounding when discounts stack

- **Round after each discount step.**
  - *Gain:* each step can be checked on its own, and it matches how the brief's combo rule rounds.
  - *Cost:* small rounding differences can build up.
- **Round once at the very end.**
  - *Gain:* closer to the exact maths.
  - *Cost:* the in-between values aren't whole cents, so they can't be stored as integers.

Both approaches give the brief's $24.30 and $10.80 examples, so those examples can't tell you which one your group chose. You'll need your own test case that shows the difference.

## 7. Satisfaction recovery

- **The customer decides whether it can recover**, based on its own state.
  - *Gain:* the rule sits with the customer's other satisfaction rules, next to the minimum of 0.
  - *Cost:* the customer needs to be told it's a Happy Hour turn.
- **The engine decides which customers recover** and simply tells them to.
  - *Gain:* the customer stays simpler.
  - *Cost:* the engine has to understand customer states, which moves domain rules into the controller.

## 8. Planning ahead versus keeping it small

- **Build only what Stage 3 needs.**
  - *Gain:* small, easy-to-explain changes, which the marking criteria reward.
  - *Cost:* less flexible for anything later.
- **Build general systems**, such as a pricing pipeline or a combo factory.
  - *Gain:* very extensible.
  - *Cost:* the brief warns against abstractions added just to satisfy a checklist, and it's more work to finish.

---

## Prompt 19

> give me ai chat history based on this chat for all 3 stages

## Reply 19

[The AI produced these three Markdown files (ai-history/stage-1/chat-01.md, ai-history/stage-2/chat-01.md, ai-history/stage-3/chat-01.md) containing the conversation above, in order.]
