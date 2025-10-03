package nordmods.primitive_multipart_entities.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

/**
 * <p>An entity that almost mimics behaviour of {@link net.minecraft.entity.boss.dragon.EnderDragonPart}.</p>
 * <p>Intended to be used as child entity of its owner. Can be used as it is or extended for more complex behaviour.
 * Has its own hitbox, but has no AI, no collision and no impact on pathfinding of the owner entity.
 * Attempting to deal damage to or interact with EntityPart results in calling respective methods for owner entity.</p>
 * <p>For an easy set up of position of child part use {@link EntityPart#setRelativePos(double, double, double, double, double, double, double, double)}.</p>
 * <p>In order to work correctly, multipart entity must implement {@link MultipartEntity} and add parts in similar fashion to {@link net.minecraft.entity.boss.dragon.EnderDragonEntity}.</p>
 */
public class EntityPart extends Entity {
    public final Entity owner;
    private final EntityDimensions hitbox;

    public EntityPart(Entity owner, float width, float height) {
        super(owner.getType(), owner.getEntityWorld());
        this.owner = owner;
        this.hitbox = EntityDimensions.changing(width, height);
        this.calculateDimensions();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    protected void readCustomData(ReadView view) {

    }

    @Override
    protected void writeCustomData(WriteView view) {

    }

    @Override
    public boolean canBeHitByProjectile() {
        return getEntityWorld().isClient() || !super.canBeHitByProjectile() ? false : owner.canBeHitByProjectile();
    }

    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return isAlwaysInvulnerableTo(source) ? false : this.owner.damage(world,  source, amount);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        return owner.interact(player, hand);
    }

    @Override
    public boolean isAlive() {
        return owner.isAlive() && super.isAlive();
    }

    @Override
    public boolean isPartOf(Entity entity) {
        return this == entity || owner == entity;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Nullable
    @Override
    public ItemStack getPickBlockStack() {
        return this.owner.getPickBlockStack();
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return hitbox;
    }

    @Override
    public boolean startRiding(Entity vehicle, boolean force, boolean emitEvent) {
        return owner.startRiding(vehicle, force, emitEvent);
    }

    @Override
    public boolean isInSameTeam(Entity entity) {
        return owner.isTeammate(entity);
    }

    @Override
    public boolean isInvisible() {
        return owner.isInvisible();
    }

    @Override
    public boolean isGlowing() {
        return owner.isGlowing();
    }

    @Override
    public boolean isInvisibleTo(PlayerEntity player) {
        return owner.isInvisibleTo(player);
    }

    @Override
    public boolean onKilledOther(ServerWorld world, LivingEntity other, DamageSource damageSource) {
        return owner.onKilledOther(world, other, damageSource);
    }

    @Override
    public boolean isAttackable() {
        return owner.isAttackable();
    }

    @Override
    public boolean handleAttack(Entity entity) {
        return owner.handleAttack(entity);
    }

    @Override
    public boolean isInvulnerable() {
        return owner.isInvulnerable() || super.isInvulnerable();
    }

    @Override
    public boolean isInPose(EntityPose pose) {
        return owner.isInPose(pose);
    }

    @Override
    public boolean hasPortalCooldown() {
        return owner.hasPortalCooldown();
    }

    @Override
    public boolean isOnGround() {
        return owner.isOnGround();
    }

    @Override
    public boolean hasNoGravity() {
        return owner.hasNoGravity();
    }

    @Override
    public boolean occludeVibrationSignals() {
        return true;
    }

    @Override
    public boolean isFireImmune() {
        return owner.isFireImmune();
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        return false;
    }

    /**
     * <p>Sets position relative to position of the owner entity.</p>
     * <p><b>x, y, z</b> - offsets relative to the owner's position (without accounting any rotations).</p>
     * <p><b>centerX, centerY, centerZ</b> - offset from owner's position relative to which part will be placed. Not affected by rotations.</p>
     * <p><b>pitch, yaw</b> - passed X and Y rotations (in degrees), relative to which offsets will be transformed.</p>
     */
    public void setRelativePos(double x, double y, double z, double centerX, double centerY, double centerZ, double pitch, double yaw) {
        lastRenderX = getX();
        lastRenderY = getY();
        lastRenderZ = getZ();

        //if you wonder how it moves - consider yourself an elliptic cylinder placed horizontally
        double cosYaw = Math.cos(-yaw * 0.017453292);
        double sinYaw = Math.sin(-yaw * 0.017453292);
        double cosPitch = Math.cos(pitch * 0.017453292);
        double sinPitch = Math.sin(pitch * 0.017453292);
        setPosition(owner.getX() + centerX + z * sinYaw * cosPitch + x * cosYaw + y * sinYaw * sinPitch,
                owner.getY() + centerY + z * -sinPitch + y * cosPitch,
                owner.getZ() + centerZ + z * cosYaw * cosPitch + x * -sinYaw + y * cosYaw * sinPitch);

        lastX = getX();
        lastY = getY();
        lastZ = getZ();
    }

    public void setRelativePos(double x, double y, double z, double centerX, double centerY, double centerZ) {
        setRelativePos(x, y ,z, centerX, centerY, centerZ, owner.getPitch(), owner.getYaw());
    }

    public void setRelativePos(double x, double y, double z, double pitch, double yaw) {
        setRelativePos(x, y ,z, 0, 0, 0, pitch, yaw);
    }

    public void setRelativePos(double x, double y, double z) {
        setRelativePos(x, y ,z, 0, 0, 0, owner.getPitch(), owner.getYaw());
    }
}
