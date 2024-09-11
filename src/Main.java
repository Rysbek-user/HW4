import java.util.Random;

public class Main {
    public static int bossHealth = 2700;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {290, 270, 250, 280, 500, 300, 0};
    public static int[] heroesDamage = {20, 15, 10, 0, 10, 25, 0};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Physical", "Physical", "None"};
    public static int roundNumber = 0;
    public static int medicHealingPower = 50;
    public static boolean isBossStunned = false;

    public static void main(String[] args) {
        printStatistics();
        while (!isGameOver()) {
            playRound();
        }
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        if (!isBossStunned) {
            bossAttacks();
        }
        heroesAttack();
        medicHeals();
        printStatistics();
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length - 1);
        bossDefence = heroesAttackType[randomIndex];
        if (heroesAttackType[randomIndex].equals("Thor")) {
            isBossStunned = random.nextBoolean();
        } else {
            isBossStunned = false;
        }
    }

    public static void bossAttacks() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                int damageToDeal = bossDamage;
                if (i == 4 && heroesHealth[4] > 0) {
                    damageToDeal /= 5;
                    heroesHealth[4] -= damageToDeal;
                }
                if (i != 4) {
                    if (heroesHealth[i] - damageToDeal < 0) {
                        heroesHealth[i] = 0;
                    } else {
                        heroesHealth[i] -= damageToDeal;
                    }
                }
            }
        }
    }

    public static void heroesAttack() {
        Random random = new Random();
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                if (heroesAttackType[i].equals("Lucky")) {
                    if (random.nextBoolean()) {
                        continue;
                    }
                }
                if (heroesAttackType[i].equals("Witcher")) {
                    continue;
                }
                int damage = heroesDamage[i];
                if (heroesAttackType[i].equals(bossDefence)) {
                    int coefficient = random.nextInt(9) + 2;
                    damage = heroesDamage[i] * coefficient;
                    System.out.println("Critical damage: " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth -= damage;
                }
            }
        }
        if (heroesAttackType[5].equals("Thor") && isBossStunned) {
            System.out.println("Boss is stunned and misses this round!");
            isBossStunned = false;
        }
    }

    public static void medicHeals() {
        if (heroesHealth[3] > 0) {
            int targetIndex = -1;
            for (int i = 0; i < heroesHealth.length - 1; i++) {
                if (heroesHealth[i] > 0 && heroesHealth[i] < 100) {
                    targetIndex = i;
                    break;
                }
            }
            if (targetIndex != -1) {
                heroesHealth[targetIndex] += medicHealingPower;
                System.out.println("Medical healing: " + heroesAttackType[targetIndex] + " " + medicHealingPower + " health points");
            }
        }
        reviveHero();
    }

    public static void reviveHero() {
        if (heroesHealth[6] > 0 && heroesHealth[6] < 1) {
            for (int i = 0; i < heroesHealth.length - 1; i++) {
                if (heroesHealth[i] > 0 && heroesHealth[i] < 100) {
                    heroesHealth[i] = heroesHealth[6];
                    heroesHealth[6] = 0;
                    System.out.println("Witcher sacrifices themselves to revive " + heroesAttackType[i] + "!");
                    break;
                }
            }
        }
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " -----------------");
        System.out.println("BOSS health: " + bossHealth + " damage: " + bossDamage
                + " defence: " + (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i]
                    + " damage: " + heroesDamage[i]);
        }
    }
}
