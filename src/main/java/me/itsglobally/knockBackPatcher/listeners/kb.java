package me.itsglobally.knockBackPatcher.listeners;

import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class kb implements Listener {

    private static final KnockbackSettings knockbackSettings = new KnockbackSettings();

    static {
        // 初始化 KnockbackSettings 配置值
        knockbackSettings.setVertical(0.9055D); // 设置垂直击退
        knockbackSettings.setHorizontal(0.25635D); // 设置水平击退
        knockbackSettings.setSprintMultiplier(1.0D); // 设置冲刺倍率
        knockbackSettings.setRangeFactor(0.025D); // 设置范围影响因子
        knockbackSettings.setMaxRangeReduction(1.2D); // 设置最大范围减少值
        knockbackSettings.setStartRangeReduction(3.0D); // 设置开始范围减少值
        knockbackSettings.setMinRange(0.12D); // 设置最小范围值
        knockbackSettings.setVerticalLimit(true); // 启用垂直限制
        knockbackSettings.setVerticalLimitValue(4.0D); // 设置垂直限制值
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();
            applyCustomKnockback(victim, attacker);
        }
    }

    private void applyCustomKnockback(Player victim, Player attacker) {
        Vector knockback = calculateKnockback(victim, attacker);
        victim.setVelocity(knockback); // 应用击退向量
    }

    private Vector calculateKnockback(Player victim, Player attacker) {
        Vector direction = victim.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize();
        double distance = victim.getLocation().distance(attacker.getLocation());

        double horizontal = knockbackSettings.getHorizontal();
        double vertical = knockbackSettings.getVertical();

        if (attacker.isSprinting()) {
            horizontal *= knockbackSettings.getSprintMultiplier();
        }

        double rangeReduction = Math.min(knockbackSettings.getMaxRangeReduction(),
                (distance - knockbackSettings.getStartRangeReduction()) * knockbackSettings.getRangeFactor());
        horizontal = Math.max(knockbackSettings.getMinRange(), horizontal - rangeReduction);

        Vector knockback = direction.multiply(horizontal).setY(vertical);

        if (knockbackSettings.isVerticalLimit() && knockback.getY() > knockbackSettings.getVerticalLimitValue()) {
            knockback.setY(knockbackSettings.getVerticalLimitValue());
        }

        return knockback;
    }

    @Data
    private static class KnockbackSettings {
        private double vertical;
        private double horizontal;
        private double sprintMultiplier;
        private double rangeFactor;
        private double maxRangeReduction;
        private double startRangeReduction;
        private double minRange;
        private boolean verticalLimit;
        private double verticalLimitValue;
    }
}
