{
  description = "A very basic flake";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";
  };

  outputs = { self, nixpkgs }: 
  let
    system = "x86_64-linux";
    pkgs = nixpkgs.legacyPackages.${system};
  in
  {
    devShells.${system}.default = pkgs.mkShell {
      buildInputs = with pkgs; [
          jdk21
          # async-profiler
          # visualvm
      ];
      shellHook = ''
        echo
        echo "Flake with jdk21."
        echo
        echo "\$ java --version"
        java --version
        echo
      '';
    };
  };
}
