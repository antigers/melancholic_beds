package antigers.melancholic_beds.config;

import antigers.melancholic_beds.MelancholicBeds;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class ServerConfigData {
    public Boolean sheepRequireShears;
    public Boolean disableWoolCrafting;
    public Boolean enableNightmares;
    public Boolean disableSleeping;
    public Boolean disableInsomnia;
    public Boolean spawnPhantomsInSwamps;

    public static final CustomPacketPayload.Type<ImmutableServerConfigData> PAYLOAD_TYPE = new CustomPacketPayload.Type<>(
            Identifier.fromNamespaceAndPath(MelancholicBeds.MOD_ID, "server_config_component")
    );
    public static final StreamCodec<ByteBuf, ImmutableServerConfigData> PAYLOAD_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ImmutableServerConfigData::toJson,
            ImmutableServerConfigData::fromJson
    );

    public static final Gson gson = new Gson();

    public record ImmutableServerConfigData (
            Boolean sheepRequireShears,
            Boolean disableWoolCrafting,
            Boolean enableNightmares,
            Boolean disableSleeping,
            Boolean disableInsomnia,
            Boolean spawnPhantomsInSwamps
    ) implements CustomPacketPayload {

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PAYLOAD_TYPE;
        }

        public String toJson() {
            return gson.toJson(this);
        }

        public static ImmutableServerConfigData fromJson(String json) {
            return gson.fromJson(json, ImmutableServerConfigData.class);
        }
    }

    public ImmutableServerConfigData getImmutable() {
        return new ImmutableServerConfigData(
                sheepRequireShears, disableWoolCrafting, enableNightmares, disableSleeping, disableInsomnia, spawnPhantomsInSwamps
        );
    }
}
