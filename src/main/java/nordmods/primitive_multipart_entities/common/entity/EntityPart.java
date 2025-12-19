package nordmods.primitive_multipart_entities.common.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

//javadoc
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

/**
 * <p>An entity that almost mimics behaviour of {@link EnderDragonPart}.</p>
 * <p>Intended to be used as child entity of its owner. Can be used as it is or extended for more complex behaviour.
 * Has its own hitbox, but has no AI, no collision and no impact on pathfinding of the owner entity.
 * Attempting to deal damage to or interact with EntityPart results in calling respective methods for owner entity.</p>
 * <p>For an easy set up of position of child part use {@link EntityPart#setRelativePos(double, double, double, double, double, double, double, double)}.</p>
 * <p>In order to work correctly, multipart entity must implement {@link MultipartEntity} and add parts in similar fashion to {@link EnderDragon}.</p>
 */
public class EntityPart extends Entity {
    public final Entity owner;
    private final EntityDimensions hitbox;

    public EntityPart(Entity owner, float width, float height) {
        super(owner.getType(), owner.level());
        this.owner = owner;
        this.hitbox = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {

    }

    @Override
    public boolean canBeHitByProjectile() {
        return !level().isClientSide() && super.canBeHitByProjectile() && owner.canBeHitByProjectile();
    }

    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        return !isInvulnerableToBase(source) && this.owner.hurtServer(world, source, amount);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        return owner.interact(player, hand);
    }

    @Override
    public boolean isAlive() {
        return owner.isAlive() && super.isAlive();
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || owner == entity;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entityTrackerEntry) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return this.owner.getPickResult();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return hitbox;
    }

    @Override
    public boolean startRiding(Entity vehicle, boolean force, boolean emitEvent) {
        return owner.startRiding(vehicle, force, emitEvent);
    }

    @Override
    public boolean considersEntityAsAlly(Entity entity) {
        return owner.isAlliedTo(entity);
    }

    @Override
    public boolean isInvisible() {
        return owner.isInvisible();
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return owner.isCurrentlyGlowing();
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        return owner.isInvisibleTo(player);
    }

    @Override
    public boolean killedEntity(ServerLevel world, LivingEntity other, DamageSource damageSource) {
        return owner.killedEntity(world, other, damageSource);
    }

    @Override
    public boolean isAttackable() {
        return owner.isAttackable();
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        return owner.skipAttackInteraction(entity);
    }

    @Override
    public boolean isInvulnerable() {
        return owner.isInvulnerable() || super.isInvulnerable();
    }

    @Override
    public boolean hasPose(Pose pose) {
        return owner.hasPose(pose);
    }

    @Override
    public boolean isOnPortalCooldown() {
        return owner.isOnPortalCooldown();
    }

    @Override
    public boolean onGround() {
        return owner.onGround();
    }

    @Override
    public boolean isNoGravity() {
        return owner.isNoGravity();
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return owner.fireImmune();
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return false;
    }

    /**
     * <p>Sets position relative to position of the owner entity.</p>
     * <p><b>x, y, z</b> - offsets relative to the owner's position (without accounting any rotations).</p>
     * <p><b>centerX, centerY, centerZ</b> - offset from owner's position relative to which part will be placed. Not affected by rotations.</p>
     * <p><b>pitch, yaw</b> - passed X and Y rotations (in degrees), relative to which offsets will be transformed.</p>
     */
    public void setRelativePos(double x, double y, double z, double centerX, double centerY, double centerZ, double rotX, double rotY) {
        setOldPos();

        //if you wonder how it moves - consider yourself an elliptic cylinder placed horizontally
        double cosYaw = Math.cos(-rotY * 0.017453292);
        double sinYaw = Math.sin(-rotY * 0.017453292);
        double cosPitch = Math.cos(rotX * 0.017453292);
        double sinPitch = Math.sin(rotX * 0.017453292);
        setPos(owner.getX() + centerX + z * sinYaw * cosPitch + x * cosYaw + y * sinYaw * sinPitch,
                owner.getY() + centerY + z * -sinPitch + y * cosPitch,
                owner.getZ() + centerZ + z * cosYaw * cosPitch + x * -sinYaw + y * cosYaw * sinPitch);
    }

    public void setRelativePos(double x, double y, double z, double centerX, double centerY, double centerZ) {
        setRelativePos(x, y ,z, centerX, centerY, centerZ, owner.getXRot(), owner.getYRot());
    }

    public void setRelativePos(double x, double y, double z, double pitch, double yaw) {
        setRelativePos(x, y ,z, 0, 0, 0, pitch, yaw);
    }

    public void setRelativePos(double x, double y, double z) {
        setRelativePos(x, y ,z, 0, 0, 0, owner.getXRot(), owner.getYRot());
    }
}
