# AI Chat History — Stage 2

- **Member:** Madu (Coder 2, Customer and Menu Domain)
- **AI tool:** Claude (Anthropic), claude.ai
- **Scope:** Stage 2 part of one continuous conversation. It follows `ai-history/stage-1/chat-01.md` and continues in `ai-history/stage-3/chat-01.md`.
- **Note:** Prompts and replies are reproduced in full and in order. Attachments are noted in square brackets. Background tool activity (the AI opening the attached files) is not shown.

---

## Prompt 8

[Attached: Stage_2_Team_Role_Allocation_Matrix.pdf]

> this is the stage 2 brief and my role is coder 2. can u explain what's new, and what should i think about?

## Reply 8

This PDF is your team's role plan for Stage 2 (written by piaa). It isn't the official brief; the official Stage 2 rules are in Section 4.2 of the assignment doc you shared earlier. Below I've kept apart what the brief requires, what your team has decided, and my own brainstorming.

## What's new in Stage 2

**What the brief requires (Section 4.2)**
- Two new customer types appear alongside Regular customers:
  - **VIP:** loses 5 satisfaction per unserved turn and pays 10% less.
  - **Critic:** loses 12 per unserved turn and pays full price. It also needs one distinct order preference or reaction to service, which your group defines and documents.
- All customer types follow the **same order and service flow**.
- Different behaviour must come from the **customer objects themselves**, not a chain of type checks in the turn controller.
- The base-setting run must include at least one VIP and one Critic, still with six arrivals. Two of the Regular customers may be replaced.
- Stage 2 must be playable on its own, with the same win and lose rules.

**What your team plan decides for your role (Coder 2)**
- New files: `VIPCustomer` and `CriticCustomer`.
- Edits to `Customer`: add `chooseItem(Menu)` and `getTypeName()`. `RegularCustomer` only gets `getTypeName()`.
- VIP bills in cents: Salad 810, Burger 1350, Pasta 1620.
- The Critic's default rule is to **always order Pasta**. The group still has to confirm this.
- By default, `chooseItem` uses the existing deterministic menu rule, and the Critic overrides it.
- Every type is kept at a minimum of 0 satisfaction and becomes ABANDONED when it reaches 0.
- Javadoc must state each customer type's rule.
- **Handoff rule 2:** you agree the two new method signatures *first*, because Members 1 and 3 call them. Merge order: you first, then Member 3, then Member 1.

## What to think about

*Everything from here on is brainstorming, not requirements.*

**1. Signatures first, because others are waiting on you**
- What exactly do `chooseItem(Menu)` and `getTypeName()` promise to the people calling them? Could either ever return nothing or fail?
- Agree on these with Members 1 and 3 before working on anything inside the methods.

**2. Is `getTypeName()` a type check in disguise?**
- It's fine for the display to show "VIP". But if any code *branches* on that string, for example "if the type is VIP, apply a discount", that brings back exactly the type checks the brief forbids.
- How will you make sure it's only used for display?

**3. What lives in the parent class and what lives in the subclasses**
- The team plan relies on `decaySatisfaction()` and `calculateFinalBill()` already existing on `Customer`. Do they exist in your Stage 1 code? If not, adding them is another change you'll need to explain in the README.
- Which behaviour should **never** differ between types, such as the minimum of 0, becoming ABANDONED and the order of states? How do you stop a subclass from accidentally changing it?
- Which behaviour **should** differ: the loss amount, the bill, and the food choice?
- This connects to substitutability (LSP) in the design discussion: can the controller treat a VIP or Critic exactly like a Regular customer?

**4. The bill**
- What does `calculateFinalBill()` work from: the menu item's price, or the order's? Who calls it? The team plan says the Cashier.
- Stage 2 needs no rounding, since every price divides evenly by 10. Stage 3 will add Happy Hour, which "locks the price when the order is taken." Don't build that now, but notice whether your design would make it awkward later.

**5. The Critic's choice and the ordering rule**
- If the Critic always orders Pasta, it ignores `(id − 1) % 3`. Where is that documented?
- With the proposed schedule (Regular, VIP, Regular, Critic, Regular, Regular), work out what dish each ID would get. Does the Critic's override change anything for the customers after it?
- The Critic arrives on turn 10, orders the 2-unit dish and loses 12 per turn. Can it realistically pay before turn 18? Trace it yourself to see whether it makes a meaningful choice for the player.
- Is "always Pasta" enough to count as distinct behaviour, or would a reaction to service be more interesting? The team plan lists this as a decision to confirm.

**6. Evidence of what changed (Stage 1 → 2)**
- For every file you edit, can you state the reason in one sentence for the README?
- Can you show that Regular customers still behave exactly as they did in Stage 1?

**7. Helping Member 5 with testing**
- Member 5 will check that a VIP abandons after 20 unserved turns, a Critic after 9, and a VIP Pasta bills $16.20. Can you confirm those numbers from your own rules before they test?

**One more thing:** the PDF requires every AI chat to be saved in order. This conversation now covers both Stage 1 and Stage 2, so check with Member 5 how to file it under `ai-history/`.

---

## Prompt 9

> what are some high-level ways VIPs and Critics could behave differently? give me options to think about, don't choose rules for me

## Reply 9

Before brainstorming, here's what the brief already fixes, so the options below don't contradict it:

- **VIP:** loses 5 per unserved turn and pays 10% less. Its "special behaviour" column in the brief is **"—"**, meaning nothing extra is required.
- **Critic:** loses 12 per unserved turn and pays full price, plus **one** distinct behaviour that your group defines and documents. The brief gives two kinds: an **order preference** or a **reaction to service**.
- **Extras:** extra behaviour only earns credit if it shows sound design and doesn't replace required rules (Section 4.4). Every type must still follow the same order and service flow.

So the real open choice is the Critic's behaviour. Anything extra for VIPs is optional.

## Critic: order preferences

*Brainstorming only.*

1. **Always the same dish**, for example the most expensive or the slowest one. This is your team's current default.
   - Simple and easy to test. The cost: the player always knows what's coming.
2. **Avoids a certain dish**, for example never orders Salad, and otherwise follows the normal rule.
   - Less predictable, but you'd need to say what happens if the normal rule lands on the avoided dish.
3. **Picks by a quality of the dish**, such as the highest price or the most preparation units.
   - Expressed as a rule rather than a fixed dish, which may cope better with ComboMeal in Stage 3.
4. **Uses a different deterministic pattern** from Regular customers, for example the menu cycle in reverse.
   - Shows overriding clearly, but the player may not notice anything different.
5. **Chooses based on the game state**, for example the dish that's quickest to prepare at that moment.
   - Interesting to play against, but harder to document and test, and it needs information from outside the customer.

## Critic: reactions to service

6. **Penalty for slow service:** an extra satisfaction loss if the Critic is served after a certain number of turns.
7. **Bonus for fast service:** gains satisfaction if served quickly.
8. **Effect on the recorded score:** the Critic's satisfaction at payment counts differently, for example rounded down or scaled.
   - Check carefully that this doesn't conflict with how the brief defines the average.
9. **Influence on other customers:** a Critic leaving unhappy affects others' satisfaction.
   - Very visible in play, but it reaches beyond one customer object and makes it harder to keep "behaviour lives in the customer."
10. **Notices waiting while seated:** the Critic reacts if seated but no order is taken for a while, separate from the standard per-turn loss.

## VIP: optional extras

11. **Nothing beyond the brief.** This is a valid choice and keeps Stage 2 focused.
12. **A preference**, such as ordering more expensive dishes, using the same overriding mechanism as the Critic.
13. **A display difference**, such as being marked as a priority in the queue, with no change to the rules.
    - Low risk, but think about whether it adds real design value or just decoration.

Anything that makes a VIP jump the queue or skip steps would break "same order and service flow," so treat that kind of idea with caution.

## Questions for judging any option

- Does it change what the **player** decides, or does it only change numbers in the background?
- Can it live entirely **inside the customer class**, without the controller checking types?
- Can Member 5 **test it reproducibly** with a fixed run?
- Does it still let a **victory run** with one VIP and one Critic succeed? Does it make a **defeat run** easy to show?
- Can you explain it in **one sentence in the Javadoc**, as your team plan requires?
- Would it make **Stage 3** harder? Think about ComboMeal choices and Happy Hour pricing.

---

## Prompt 10

> what should i consider when adding these customer types without breaking stage 1?

## Reply 10

The main risk is that Regular customers behave slightly differently after your changes, or that teammates' Stage 1 code stops compiling. Here's what's worth checking, starting with what the docs require.

## What the docs require

- **The `stage-1` tag never moves.** Stage 2 is built on top of it, and the tagged version must stay runnable. *(team plan, handoff rule 1; brief Section 4)*
- **Regular customers keep their Stage 1 rules:** loss of 8, full price, the same state flow. *(brief 4.2)*
- **All types follow the same order and service flow,** with no type checks in the controller. *(brief 4.2)*
- **Every changed file is listed in the README with a reason**, and you need evidence that earlier behaviour still works. *(brief 4.4 and Section 5; briefing slide 4)*

## What to consider

*Brainstorming from here on.*

**1. Moving rules without changing them**
- If the number 8 or the full-price bill currently sits in `Customer`, does it move to `RegularCustomer`, or stay as the default? Either way, does a Regular customer end up with exactly the same numbers as before?
- Is the Stage 1 ordering rule still exactly `(id − 1) % 3` for Regular customers when it's reached through the new `chooseItem`?

**2. Changes that can break other people's code**
- If `Customer` becomes abstract, any Stage 1 code that creates a plain `Customer` will stop compiling. Where is that done today?
- Are you **adding** methods, or **renaming or changing** existing ones? Adding is safer, because changing an existing method breaks everyone who calls it.
- **Merge order:** you merge first. Until Member 3 updates `Waiter` to call `chooseItem`, will the old way of choosing food still work and compile? Is there a moment where the merged code won't build?

**3. Shared behaviour stays in one place**
- The minimum of 0, becoming ABANDONED at 0, and the order of states: are these written once in `Customer`, or copied into each subclass? Copies can drift apart over time.
- Can a subclass accidentally override something it shouldn't, like how states change?
- Does each subclass change **only** what makes it different: the loss amount, the bill and the food choice?

**4. IDs and arrivals**
- When a VIP or Critic replaces a Regular arrival, are IDs still given out in arrival order across all types? Or do they now count per type?
- What dish does each Regular customer get now? Is that what your group expects, and is it documented?

**5. Proving nothing broke**
- The Stage 2 base run uses a different arrival schedule, so the Stage 1 victory and defeat inputs won't replay identically. How will you show that Regular behaviour didn't change?
  - **Option:** follow one Regular customer through the Section 3.3 example and get the same numbers as before.
  - **Option:** compare a few situations at `stage-1` with the same situations in Stage 2.
  - **Option:** Member 5 re-runs the Stage 1 tests on the old tag and spot-checks Regular customers in Stage 2.
- Which of these would be convincing evidence in the report and video?

**6. Keeping the scope clean**
- Is anything you add really Stage 3 work in disguise, such as a general pricing system for Happy Hour or combos? General support is allowed, but be ready to explain why it's there.
- Does `getTypeName()` stay a display-only label, never something the code branches on?

**7. Documentation**
- Can you write one sentence for each file you changed, explaining why? The README needs exactly that.
- Does the Javadoc on each customer type state its rule clearly enough for Member 5 to test from it?

---

## Prompt 11

> checklist for stage 2

## Reply 11

Here's a Stage 2 checklist for your role, set out like the Stage 1 one. Each item says where it comes from: **[Brief]** is the assignment doc and **[Team]** is the role allocation matrix. Team items are group decisions, so check them against what your group finally agrees.

## Before coding

- [ ] **Signatures agreed with Members 1 and 3:** `chooseItem(Menu)` and `getTypeName()` on `Customer`. *[Team, handoff 2]*
- [ ] **Critic behaviour confirmed by the group**, either "always Pasta" or something else. *[Brief 4.2; Team, "Decisions to confirm"]*
- [ ] **Your branch starts from `stage-1`,** and that tag is never moved. *[Team, handoff 1]*

## VIPCustomer

- [ ] Loses **5** satisfaction per unserved turn. *[Brief 4.2]*
- [ ] Bill is **10% off**, in integer cents: 810, 1350, 1620. *[Brief 4.2, 2.2; Team]*
- [ ] No other special behaviour is required. If you add any, it must not replace required rules. *[Brief 4.2, 4.4]*

## CriticCustomer

- [ ] Loses **12** satisfaction per unserved turn. *[Brief 4.2]*
- [ ] Pays **full price**. *[Brief 4.2]*
- [ ] Has **one** distinct order preference or reaction to service, which is documented. *[Brief 4.2]*
- [ ] If it's "always Pasta", the Critic's choice overrides the default rule. *[Team]*

## Every customer type

- [ ] The same order and service flow, and the same states. *[Brief 4.2]*
- [ ] Satisfaction never goes below **0**, and a customer at 0 becomes **ABANDONED**. *[Brief 3.2; Team]*
- [ ] Customers who are served, eating or ready to pay lose nothing. *[Brief 3.2]*
- [ ] By default, `chooseItem` uses the Stage 1 deterministic rule. *[Team]*
- [ ] Different behaviour comes from the customer objects themselves, with **no type checks** in the controller. *[Brief 4.2]*
- [ ] `getTypeName()` gives "Regular", "VIP" or "Critic" for display. *[Team]*
- [ ] **Javadoc** states each type's rule. *[Team]*

## Not breaking Stage 1

- [ ] Regular customers still lose 8, pay full price and pick food with the same rule. *[Brief 4.2]*
- [ ] The `stage-1` tag is unchanged and still runs. *[Brief 4]*
- [ ] Stage 2 contains no Host, ComboMeal or Happy Hour. *[Team, handoff 4]*
- [ ] The merged code compiles after your merge, before Member 3's changes arrive. *[Team, merge order]*

## Evidence

- [ ] Every new or changed file is listed with a reason for the README. *[Brief Section 5; Team, handoff 5]*
- [ ] Member 5's checks agree with your rules: *[Team]*
  - a VIP abandons after 20 unserved turns
  - a Critic abandons after 9
  - a VIP's Pasta bills $16.20
  - the Critic orders Pasta
- [ ] Your AI chat for Stage 2 is saved under `ai-history/stage-2/`. *[briefing PDF; Team]*

## Worth checking too (brainstorming)

- Did any Stage 1 code that creates customers or calls customer methods need changing? Is that change explained?
- Is the shared logic, such as the minimum of 0 and becoming ABANDONED, written once rather than copied into each subclass?
- With the final arrival schedule, does each customer ID get the dish your group expects?
- Can a victory run with one VIP and one Critic actually be won?

---

## Prompt 12

> what are a few things i could consider when deciding how VIPs and Critics should behave? keep it high level explain in normal terms

## Reply 12

*All brainstorming; the choices are yours.*

**Does it change what the player decides?** A good behaviour gives the manager something new to weigh up. For example, if the player has to decide whether to rush the Critic or look after the VIP first, that's meaningful. If the difference only changes numbers the player never notices, it adds little to the game.

**Is it easy to explain?** You should be able to describe each type in one plain sentence, like "the Critic loses patience faster and always wants the expensive dish." If it takes a paragraph, it will be harder to document, test and present in the video.

**Can the customer handle it by itself?** The neatest behaviours belong entirely to the customer: how fast they lose patience, what they order, how much they pay. Behaviour that needs the restaurant to treat that customer specially is harder to keep out of the controller, and the brief says the controller shouldn't check customer types.

**Is the game still fair?** With one VIP and one Critic in the run, a careful player should still be able to win, and a careless one should be able to lose. If the Critic makes winning nearly impossible, or makes no difference at all, the balance is off.

**Can it be tested the same way every time?** Member 5 has to record exact victory and defeat runs. Anything random, or anything that depends on lots of moving parts, makes that harder.

**Does it stay within the brief?** The VIP's rules are already fully set by the brief. The Critic needs exactly one distinct behaviour of your choosing. Every type still follows the same steps from arrival to payment.

**Will it fit later?** Stage 3 adds combo meals and Happy Hour pricing. A behaviour like "always orders the most expensive thing" adapts naturally to combos. One tied to a single fixed dish might need rethinking.

---

## Prompt 13

> what requirements should i check my code against?

## Reply 13

Here are the requirements to check your Stage 2 customer code against. It only includes what the documents actually say, with no suggestions of mine. **[Brief]** means the assignment doc and **[Team]** means your role allocation matrix; team items are group decisions rather than course rules.

**VIP**
- Loses 5 satisfaction for each turn it waits unserved. [Brief 4.2]
- Pays 10% less: 810, 1350 or 1620 cents. [Brief 4.2; Team]

**Critic**
- Loses 12 satisfaction for each turn it waits unserved. [Brief 4.2]
- Pays full price. [Brief 4.2]
- Has one distinct order preference or reaction to service, documented. [Brief 4.2] Your team's default is "always orders Pasta." [Team]

**Regular (unchanged from Stage 1)**
- Loses 8 per unserved turn, pays full price and uses the same food rule as before. [Brief 3.2, 4.2]

**All customer types**
- Start at satisfaction 100. [Brief 3.2]
- Lose nothing once served, while eating, or while ready to pay. [Brief 3.2]
- Never go below 0. At 0 they leave without paying, their order is cancelled and their table is freed. [Brief 3.2]
- Follow the same steps and states from arrival to payment. [Brief 4.2]
- Choose their own food by a documented, deterministic or seeded rule. The choice is shown when the order is placed. [Brief 2.2]
- Money is stored as integer cents. [Brief 2.2]
- `chooseItem(Menu)` and `getTypeName()` exist on `Customer`. [Team]
- Javadoc states each type's rule. [Team]

**Design**
- Behaviour differences come from the customer objects, not type checks in the controller. [Brief 4.2]
- No game rules in the UI. [Brief 2.2]
- No Stage 3 features: Host, ComboMeal or Happy Hour. [Brief 4.1–4.3; Team]
- Any change to a Stage 1 class has an explained reason. [Brief 4.4]

**Stage as a whole**
- Stage 2 compiles and runs on its own, tagged `stage-2`. [Brief 4.2, Section 5]
- The base run has six arrivals, including at least one VIP and one Critic. [Brief 4.2]
- The same win and lose rules as Stage 1. [Brief 4.2]

A good quick test is Member 5's list from the team plan: a VIP abandons after 20 unserved turns, a Critic after 9, a VIP's Pasta bills $16.20, and the Critic orders Pasta. If your code gives those four results, most of the customer rules above are working.
