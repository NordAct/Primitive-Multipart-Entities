package nordmods.primitive_multipart_entities.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityPart extends Entity {
    public final Entity owner;
    private final EntityDimensions hitbox;

    public EntityPart(Entity owner, float width, float height) {
        super(owner.getType(), owner.getWorld());
        this.owner = owner;
        this.hitbox = EntityDimensions.changing(width, height);
        this.calculateDimensions();
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    protected void initDataTracker() {}

    @Override
    public boolean damage(DamageSource source, float amount) {
        return getWorld().isClient() || isInvulnerableTo(source) ? false : owner.damage(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return owner.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPartOf(Entity entity) {
        return this == entity || owner == entity;
    }

    @Override
    public @NotNull Packet<?> createSpawnPacket() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        return owner.interact(player, hand);
    }

    @Override
    public boolean isAlive() {
        return owner.isAlive() && super.isAlive();
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
    public boolean startRiding(Entity vehicle, boolean force) {
        return owner.startRiding(vehicle, force);
    }

    @Override
    public boolean isTeammate(Entity entity) {
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
    public boolean onKilledOther(ServerWorld level, LivingEntity entity) {
        return owner.onKilledOther(level, entity);
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

    public void setRelativePos(double x, double y, double z, float pitch, float yaw) {
        lastRenderX = getX();
        lastRenderY = getY();
        lastRenderZ = getZ();
        Vec3d rot = getRotationVector(pitch, yaw);
        setPosition(owner.getX() + x * rot.z + z * rot.x,
                owner.getY() + y,
                owner.getZ() + z * rot.z - x * rot.x);
        Vec3d vec3ds = new Vec3d(getX(), getY(),getZ());
        prevX = vec3ds.x;
        prevY = vec3ds.y;
        prevZ = vec3ds.z;
    }

    public void setRelativePos(double x, double y, double z) {
        setRelativePos(x, y ,z, owner.getPitch(), owner.getYaw());
    }

    public void setRelativePos(Vec3f vector3f) {
        setRelativePos(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }

    public void setRelativePos(Vec3f vector3f, float pitch, float yaw) {
        setRelativePos(vector3f.getX(), vector3f.getY(), vector3f.getZ(), pitch, yaw);
    }

    public void setRelativePos(Vec3d vec3d) {
        setRelativePos(vec3d.x, vec3d.y, vec3d.z);
    }

    public void setRelativePos(Vec3d vec3d, float pitch, float yaw) {
        setRelativePos(vec3d.x, vec3d.y, vec3d.z, pitch, yaw);
    }
}
