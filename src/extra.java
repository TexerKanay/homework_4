
import java.util.Random;

public class extra {
    public static int bossHealth = 850;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {270, 260, 250, 200, 100, 150, 300, 180};
    public static int[] heroesDamage = {20, 15, 10, 0, 5, 10, 10, 7};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Lucky", "Thor", "Golem", "Witcher"};
    public static int roundNumber;
    public static int medicHealAmount = 30;
    public static boolean bossStunned = false;
    public static boolean thorUsedStun = false;

    public static void main(String[] args) {
        printStatistics();

        while (!isGameOver()) {
            playRound();
        }
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

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        bossAttack();
        heroesAttack();
        medicHeal();
        witcherRevive();
        printStatistics();
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length - 2);
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void bossAttack() {
        if (bossStunned) {
            System.out.println("The boss is stunned and skips this round!");
            bossStunned = false;
            return;
        }

        Random random = new Random();
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if (heroesAttackType[i].equals("Lucky") && random.nextBoolean()) {
                    System.out.println("Lucky dodged the attack!");
                } else {
                    int damageTaken = bossDamage;

                    if (!heroesAttackType[i].equals("Golem") && heroesHealth[6] > 0) {
                        int damageToGolem = damageTaken / 5;
                        heroesHealth[6] -= damageToGolem; // Golem получает 1/5 урона
                        damageTaken -= damageToGolem;
                    }

                    if (heroesHealth[i] - damageTaken < 0) {
                        heroesHealth[i] = 0;
                    } else {
                        heroesHealth[i] -= damageTaken;
                    }
                }
            }
        }
    }

    public static void heroesAttack() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (heroesAttackType[i].equals("Thor") && !thorUsedStun) {
                    Random random = new Random();
                    if (random.nextBoolean()) { // 50% шанс оглушить
                        bossStunned = true;
                        thorUsedStun = true; // Оглушение используется только один раз
                        System.out.println("Thor stunned the boss!");
                    }
                }
                if (heroesAttackType[i] == bossDefence) {
                    Random random = new Random();
                    int coeff = random.nextInt(9) + 2;
                    damage = damage * coeff;
                    System.out.println("Critical damage: " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth -= damage;
                }
            }
        }
    }

    public static void medicHeal() {
        int medicIndex = 3;
        if (heroesHealth[medicIndex] <= 0) return;

        for (int i = 0; i < heroesHealth.length; i++) {
            if (i != medicIndex && heroesHealth[i] > 0 && heroesHealth[i] < 100) {
                heroesHealth[i] += medicHealAmount;
                System.out.println("Medic healed " + heroesAttackType[i] + " for " + medicHealAmount + " health points");
                break; // Лечим только одного героя за раунд
            }
        }
    }

    // Метод для оживления первого погибшего героя
    public static void witcherRevive() {
        int witcherIndex = 7;
        if (heroesHealth[witcherIndex] <= 0) return; // Witcher не может оживить, если он мертв

        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] == 0) {
                heroesHealth[i] = heroesHealth[witcherIndex];
                heroesHealth[witcherIndex] = 0;
                System.out.println(heroesAttackType[i] + " was revived by Witcher!");
                break;
            }
        }
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " ---------------");
        System.out.println("BOSS health: " + bossHealth + " damage: " + bossDamage
                + " defence: " + (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] +
                    " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
    }
}



