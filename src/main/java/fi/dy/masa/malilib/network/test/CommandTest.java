package fi.dy.masa.malilib.network.test;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fi.dy.masa.malilib.MaLiLib;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandTest
{
    public static void registerCommandTest()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("malilib-network-test")
                        .then(literal("client")
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(ctx -> testPlayer(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), "", ctx))
                                        .then(argument("message", StringArgumentType.greedyString())
                                                .executes(ctx -> testPlayer(ctx.getSource(), EntityArgumentType.getPlayer(ctx,"player"), StringArgumentType.getString(ctx, "message"), ctx))
                                        )
                                )
                        )
                        .then(literal("server")
                                .executes(ctx -> testServer(ctx.getSource(), ctx))
                        )
        ));
    }

    private static int testPlayer(ServerCommandSource src, ServerPlayerEntity target, String message, CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException
    {
        String user = src.getPlayerOrThrow().getName().getLiteralString();
        if (target != null)
        {
            // Run S2C test -> Player
            TestSuite.testS2C(target, message);
        }
        else {
            // Run C2S test
            TestSuite.testC2S(message);
        }
        MaLiLib.printDebug("testPlayer(): --> Executed!");
        return 1;
    }
    private static int testServer(ServerCommandSource src, CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException
    {
        String user = src.getPlayerOrThrow().getName().getLiteralString();
        // Run C2S test
        TestSuite.testC2S("Random server message");
        MaLiLib.printDebug("testServer(): --> Executed!");
        return 1;
    }
}
